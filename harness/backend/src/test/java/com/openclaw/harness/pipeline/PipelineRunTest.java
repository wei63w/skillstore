package com.openclaw.harness.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PipelineRunTest {

    @Test
    void recordsStageResults() {
        PipelineRun run = new PipelineRun("run-1", "specs/demo", ".", PipelineRunStatus.RUNNING,
                PipelineStageType.GENERATE, 0, "report.json", List.of());

        assertThat(run.runId()).isEqualTo("run-1");
    }
}
