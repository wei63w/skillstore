package com.openclaw.harness.retry;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.HumanGateService;
import org.junit.jupiter.api.Test;

class RetryPolicyTest {

    private final RetryPolicy retryPolicy = new RetryPolicy();

    @Test
    void limitsAutoRepairToThreeAttempts() {
        assertThat(retryPolicy.canRetry(1)).isTrue();
        assertThat(retryPolicy.canRetry(2)).isTrue();
        assertThat(retryPolicy.canRetry(3)).isFalse();
    }

    @Test
    void escalatesToHumanGateWhenAttemptsExhausted() {
        RetryOrchestrator orchestrator = new RetryOrchestrator(retryPolicy, new HumanGateService());

        RetryOutcome outcome = orchestrator.handleFailure("task-1", "phase-1", "test failed", 3);

        assertThat(outcome.attempts()).hasSize(3);
        assertThat(outcome.humanGate()).isNotNull();
        assertThat(outcome.humanGate().topic()).isEqualTo("自动修复失败");
    }
}
