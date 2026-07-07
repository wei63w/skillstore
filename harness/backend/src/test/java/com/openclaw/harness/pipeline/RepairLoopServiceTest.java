package com.openclaw.harness.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RepairLoopServiceTest {

    @Test
    void opensHumanGateAfterThirdAttempt() {
        RepairLoopDecision decision = new RepairLoopService().decide("run-1", 3, PipelineStageType.BACKEND_TEST, "failed");

        assertThat(decision.repairable()).isFalse();
        assertThat(decision.nextAction()).isEqualTo("OPEN_HUMAN_GATE");
    }
}
