package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.generation.GenerationContextLoader;
import com.openclaw.harness.generation.PatchReview;
import com.openclaw.harness.generation.PatchReviewService;
import com.openclaw.harness.model.CodeModelRequest;
import com.openclaw.harness.model.CodeModelProviderRegistry;
import com.openclaw.harness.model.ModelOutputSchemaValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatchReviewController {

    private final CodeModelProviderRegistry providerRegistry;
    private final GenerationContextLoader contextLoader;
    private final ModelOutputSchemaValidator schemaValidator;
    private final PatchReviewService reviewService;

    public PatchReviewController(CodeModelProviderRegistry providerRegistry, GenerationContextLoader contextLoader, ModelOutputSchemaValidator schemaValidator, PatchReviewService reviewService) {
        this.providerRegistry = providerRegistry;
        this.contextLoader = contextLoader;
        this.schemaValidator = schemaValidator;
        this.reviewService = reviewService;
    }

    @PostMapping("/api/harness/code-generation/reviews")
    public ApiResponse<PatchReview> review(@Valid @RequestBody PatchReviewRequest request) {
        var context = contextLoader.load(Path.of(request.featureDirectory()).toAbsolutePath().normalize());
        var response = providerRegistry.get(request.modelProvider()).generatePatch(new CodeModelRequest(
                request.modelProvider(),
                request.taskId(),
                context,
                List.of("输出结构化 PatchPlan", "生成 diff review")
        ));
        var validation = schemaValidator.validate(response.patchPlan());
        return ApiResponse.ok(reviewService.review(request.requestId(), response.patchPlan(), validation));
    }

    public record PatchReviewRequest(
            @NotBlank String requestId,
            @NotBlank String taskId,
            @NotBlank String featureDirectory,
            @NotBlank String workspaceRoot,
            @NotBlank String modelProvider
    ) {
    }
}
