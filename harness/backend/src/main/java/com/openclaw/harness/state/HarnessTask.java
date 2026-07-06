package com.openclaw.harness.state;

import java.time.Instant;

public record HarnessTask(
        String taskId,
        String title,
        String objective,
        String requestedBy,
        String currentPhase,
        TaskStatus status,
        String checkpointRef,
        Instant createdAt,
        Instant updatedAt
) {
}
