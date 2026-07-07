package com.openclaw.harness.pipeline;

import java.util.List;

public record PipelineRun(
        String runId,
        String featureDirectory,
        String targetWorkspace,
        PipelineRunStatus status,
        PipelineStageType currentStage,
        int repairCount,
        String reportPath,
        List<PipelineStageResult> stages
) {
}
