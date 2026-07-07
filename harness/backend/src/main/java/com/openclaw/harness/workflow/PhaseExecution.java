package com.openclaw.harness.workflow;

import java.time.Instant;
import java.util.List;

public record PhaseExecution(
        String phaseId,
        String taskId,
        SpecKitStage stage,
        PhaseStatus status,
        String inputSummary,
        List<String> outputPaths,
        Instant startedAt,
        Instant finishedAt,
        long elapsedMs,
        String errorSummary
) {
}
