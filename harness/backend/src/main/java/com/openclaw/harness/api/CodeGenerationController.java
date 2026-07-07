package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.executors.CodeGenerationExecutor;
import com.openclaw.harness.generation.CodeGenerationRequest;
import com.openclaw.harness.generation.GenerationMode;
import com.openclaw.harness.generation.PatchApplyResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeGenerationController {

    private static final List<String> DEFAULT_ALLOWED_ROOTS = List.of(
            "skill-store/backend",
            "skill-store/frontend",
            "skill-store/README.md",
            "docs",
            "infra"
    );

    private final CodeGenerationExecutor codeGenerationExecutor;

    public CodeGenerationController(CodeGenerationExecutor codeGenerationExecutor) {
        this.codeGenerationExecutor = codeGenerationExecutor;
    }

    @PostMapping("/api/harness/code-generation")
    public ApiResponse<PatchApplyResult> generate(@Valid @RequestBody CodeGenerationApiRequest request) {
        CodeGenerationRequest generationRequest = new CodeGenerationRequest(
                request.requestIdOrDefault(),
                request.taskId(),
                Path.of(request.featureDirectory()).toAbsolutePath().normalize(),
                Path.of(request.workspaceRoot()).toAbsolutePath().normalize(),
                request.allowedRootsOrDefault(),
                request.modeOrDefault(),
                request.modelProviderOrDefault()
        );
        return ApiResponse.ok(codeGenerationExecutor.generate(generationRequest));
    }

    public record CodeGenerationApiRequest(
            String requestId,
            @NotBlank String taskId,
            @NotBlank String featureDirectory,
            @NotBlank String workspaceRoot,
            List<String> allowedRoots,
            String mode,
            String modelProvider
    ) {

        String requestIdOrDefault() {
            return requestId == null || requestId.isBlank() ? "codegen-" + UUID.randomUUID() : requestId;
        }

        List<String> allowedRootsOrDefault() {
            return allowedRoots == null || allowedRoots.isEmpty() ? DEFAULT_ALLOWED_ROOTS : allowedRoots;
        }

        GenerationMode modeOrDefault() {
            return mode == null || mode.isBlank() ? GenerationMode.DRY_RUN : GenerationMode.valueOf(mode.toUpperCase());
        }

        String modelProviderOrDefault() {
            return modelProvider == null || modelProvider.isBlank() ? "stub" : modelProvider;
        }
    }
}
