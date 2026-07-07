package com.openclaw.harness.pipeline;

import java.time.Instant;

public record PipelineStageResult(
        String stageId,
        String runId,
        PipelineStageType stageType,
        PipelineStageStatus status,
        Instant startedAt,
        Instant finishedAt,
        String evidencePath,
        String failureSummary
) {
}
