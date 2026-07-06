package com.openclaw.harness.api.dto;

import com.openclaw.harness.state.HarnessTask;
import com.openclaw.harness.state.TaskStatus;

public record TaskResponse(String taskId, String title, TaskStatus status, String currentPhase, String checkpointRef) {

    public static TaskResponse from(HarnessTask task) {
        return new TaskResponse(task.taskId(), task.title(), task.status(), task.currentPhase(), task.checkpointRef());
    }
}
