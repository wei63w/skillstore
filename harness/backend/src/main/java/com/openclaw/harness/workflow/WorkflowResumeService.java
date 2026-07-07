package com.openclaw.harness.workflow;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class WorkflowResumeService {

    private final WorkflowStateStore stateStore;
    private final SpecKitWorkflowService workflowService;

    public WorkflowResumeService(WorkflowStateStore stateStore, SpecKitWorkflowService workflowService) {
        this.stateStore = stateStore;
        this.workflowService = workflowService;
    }

    public ResumeResult resume(String taskId) {
        Instant startedAt = Instant.now();
        Path checkpoint = Path.of("harness", "runtime", "tasks", taskId, "checkpoint.json");
        DevelopmentTask task = stateStore.read(checkpoint, DevelopmentTask.class)
                .orElseThrow(() -> new IllegalArgumentException("未找到可恢复检查点: " + taskId));
        workflowService.restoreInMemory(task);
        long elapsedMs = Duration.between(startedAt, Instant.now()).toMillis();
        return new ResumeResult(task.taskId(), task.currentStage(), task.currentStage(), elapsedMs);
    }
}
