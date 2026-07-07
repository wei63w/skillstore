package com.openclaw.harness.pipeline;

public record RepairLoopDecision(
        String repairId,
        String runId,
        int attemptNumber,
        PipelineStageType failureStage,
        String failureSummary,
        boolean repairable,
        String nextAction
) {
}
