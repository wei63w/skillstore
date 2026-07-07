package com.openclaw.harness.generation;

public record FileChange(
        String path,
        String changeType,
        String content,
        String taskId,
        String reason
) {
}
