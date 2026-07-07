package com.openclaw.harness.workflow;

public record ResumeResult(
        String taskId,
        SpecKitStage resumedFrom,
        SpecKitStage nextStage,
        long restoreElapsedMs
) {
}
