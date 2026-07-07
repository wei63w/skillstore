package com.openclaw.harness.workflow;

import java.time.Instant;

public record DevelopmentTask(
        String taskId,
        String objective,
        String featureDirectory,
        SpecKitStage currentStage,
        WorkflowStatus status,
        String checkpointPath,
        Instant createdAt,
        Instant updatedAt
) {
}
