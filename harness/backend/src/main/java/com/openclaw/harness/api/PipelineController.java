package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.pipeline.PipelineRun;
import com.openclaw.harness.pipeline.PipelineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping("/api/harness/pipelines")
    public ResponseEntity<ApiResponse<PipelineRun>> start(@Valid @RequestBody PipelineStartRequest request) {
        PipelineRun run = pipelineService.dryRun(request.runId(), Path.of(request.featureDirectory()), Path.of(request.workspaceRoot()));
        return ResponseEntity.status(202).body(ApiResponse.ok(run));
    }

    public record PipelineStartRequest(
            @NotBlank String runId,
            @NotBlank String featureDirectory,
            @NotBlank String workspaceRoot,
            String modelProvider,
            boolean dryRun
    ) {
    }
}
