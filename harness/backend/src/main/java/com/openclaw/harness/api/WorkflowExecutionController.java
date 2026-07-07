package com.openclaw.harness.api;

import com.openclaw.harness.api.dto.WorkflowResponses.WorkflowResponse;
import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.executors.CommandResult;
import com.openclaw.harness.executors.TestExecutor;
import com.openclaw.harness.executors.TestProfile;
import com.openclaw.harness.workflow.SpecKitWorkflowService;
import java.nio.file.Path;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowExecutionController {

    private final SpecKitWorkflowService workflowService;
    private final TestExecutor testExecutor;

    public WorkflowExecutionController(SpecKitWorkflowService workflowService, TestExecutor testExecutor) {
        this.workflowService = workflowService;
        this.testExecutor = testExecutor;
    }

    @PostMapping("/api/harness/workflows/{taskId}/run-tasks")
    public ApiResponse<WorkflowResponse> runTasks(@PathVariable String taskId) {
        return ApiResponse.ok(WorkflowResponse.from(workflowService.get(taskId)));
    }

    @PostMapping("/api/harness/workflows/{taskId}/tests")
    public ApiResponse<TestRunResponse> runTests(
            @PathVariable String taskId,
            @RequestParam(defaultValue = "UNIT") TestProfile profile
    ) {
        workflowService.get(taskId);
        CommandResult result = testExecutor.run(Path.of(".").toAbsolutePath().normalize(), Path.of(".").toAbsolutePath().normalize(), profile);
        return ApiResponse.ok(new TestRunResponse(taskId, profile.name().toLowerCase(), result.successful() ? "passed" : "failed", result.exitCode()));
    }

    public record TestRunResponse(String taskId, String profile, String status, int exitCode) {
    }
}
