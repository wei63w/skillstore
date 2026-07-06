package com.openclaw.harness.observability;

import java.time.Instant;

public record ExecutionEvent(
        String stepId,
        String taskId,
        String agentType,
        String action,
        String inputRef,
        String outputRef,
        String toolCallRef,
        long elapsedMs,
        ExecutionResult result,
        Instant createdAt
) {
}
