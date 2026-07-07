package com.openclaw.harness.api;

import com.openclaw.harness.api.dto.WorkflowRequests.StartWorkflowRequest;
import com.openclaw.harness.api.dto.WorkflowResponses.PhaseExecutionResponse;
import com.openclaw.harness.api.dto.WorkflowResponses.ResumeResponse;
import com.openclaw.harness.api.dto.WorkflowResponses.WorkflowResponse;
import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.workflow.SpecKitWorkflowService;
import com.openclaw.harness.workflow.WorkflowResumeService;
import jakarta.validation.Valid;
import java.nio.file.Path;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowController {

    private final SpecKitWorkflowService workflowService;
    private final WorkflowResumeService resumeService;

    public WorkflowController(SpecKitWorkflowService workflowService, WorkflowResumeService resumeService) {
        this.workflowService = workflowService;
        this.resumeService = resumeService;
    }

    @PostMapping("/api/harness/workflows")
    public ResponseEntity<ApiResponse<WorkflowResponse>> start(@Valid @RequestBody StartWorkflowRequest request) {
        String featureDirectory = request.featureDirectory() == null || request.featureDirectory().isBlank()
                ? "specs/004-harness-automation"
                : request.featureDirectory();
        WorkflowResponse response = WorkflowResponse.from(workflowService.start(
                request.objective(),
                Path.of(request.workspace()).toAbsolutePath().normalize(),
                featureDirectory
        ));
        return ResponseEntity.status(202).body(ApiResponse.ok(response));
    }

    @PostMapping("/api/harness/workflows/{taskId}/resume")
    public ApiResponse<ResumeResponse> resume(@PathVariable String taskId) {
        return ApiResponse.ok(ResumeResponse.from(resumeService.resume(taskId)));
    }

    @GetMapping("/api/harness/workflows/{taskId}/phases")
    public ApiResponse<List<PhaseExecutionResponse>> phases(@PathVariable String taskId) {
        return ApiResponse.ok(workflowService.phases(taskId).stream()
                .map(PhaseExecutionResponse::from)
                .toList());
    }
}
