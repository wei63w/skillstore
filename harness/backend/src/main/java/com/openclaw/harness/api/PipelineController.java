package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.pipeline.PipelineRun;
import com.openclaw.harness.pipeline.PipelineService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping("/api/harness/pipelines")
    public ResponseEntity<ApiResponse<PipelineRun>> start(@Valid @RequestBody PipelineStartRequest request) {
        String runId = request.runId() == null || request.runId().isBlank()
                ? "run-" + UUID.randomUUID().toString().substring(0, 8)
                : request.runId();
        Path workspaceRoot = request.workspaceRoot() == null || request.workspaceRoot().isBlank()
                ? Path.of(".").toAbsolutePath().normalize().getParent().getParent()
                : Path.of(request.workspaceRoot());
        Path featureDirectory = request.featureDirectory() == null || request.featureDirectory().isBlank()
                ? workspaceRoot.resolve("specs/007-real-harness-console")
                : Path.of(request.featureDirectory());
        PipelineRun run = request.dryRun()
                ? pipelineService.dryRun(runId, featureDirectory, workspaceRoot)
                : pipelineService.run(runId, featureDirectory, workspaceRoot);
        return ResponseEntity.status(202).body(ApiResponse.ok(run));
    }

    @GetMapping("/api/harness/pipelines")
    public ResponseEntity<ApiResponse<List<PipelineRun>>> list(@RequestParam(required = false) String workspaceRoot) {
        return ResponseEntity.ok(ApiResponse.ok(pipelineService.list(resolveWorkspaceRoot(workspaceRoot))));
    }

    @GetMapping("/api/harness/pipelines/{runId}")
    public ResponseEntity<ApiResponse<PipelineRun>> detail(@PathVariable String runId, @RequestParam(required = false) String workspaceRoot) {
        return pipelineService.find(resolveWorkspaceRoot(workspaceRoot), runId)
                .map(run -> ResponseEntity.ok(ApiResponse.ok(run)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Pipeline run 不存在: " + runId, java.time.Instant.now())));
    }

    @GetMapping("/api/harness/pipelines/{runId}/logs")
    public ResponseEntity<ApiResponse<String>> logs(@PathVariable String runId, @RequestParam(required = false) String workspaceRoot) {
        return ResponseEntity.ok(ApiResponse.ok(pipelineService.logs(resolveWorkspaceRoot(workspaceRoot), runId)));
    }

    private Path resolveWorkspaceRoot(String workspaceRoot) {
        return workspaceRoot == null || workspaceRoot.isBlank() ? defaultWorkspaceRoot() : Path.of(workspaceRoot);
    }

    private Path defaultWorkspaceRoot() {
        String configuredRoot = System.getenv("HARNESS_WORKSPACE_ROOT");
        if (configuredRoot != null && !configuredRoot.isBlank()) {
            return Path.of(configuredRoot).toAbsolutePath().normalize();
        }
        Path current = Path.of(".").toAbsolutePath().normalize();
        if (current.endsWith(Path.of("harness", "backend"))) {
            return current.getParent().getParent();
        }
        return current;
    }

    public record PipelineStartRequest(
            String runId,
            String featureDirectory,
            String workspaceRoot,
            String modelProvider,
            boolean dryRun
    ) {
    }
}
