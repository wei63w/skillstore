package com.openclaw.harness.sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SandboxPolicyServiceTest {

    private final SandboxPolicyService service = new SandboxPolicyService();

    @Test
    void allowsNormalFileOperationAndBlocksUnsafePath() {
        SandboxDecision allowed = service.evaluate(new SandboxOperation("run-1", SandboxOperationType.FILE, "skill-store/README.md"));
        SandboxDecision denied = service.evaluate(new SandboxOperation("run-1", SandboxOperationType.FILE, "../.env"));

        assertThat(allowed.decision()).isEqualTo(SandboxDecisionType.ALLOW);
        assertThat(denied.decision()).isEqualTo(SandboxDecisionType.DENY);
    }

    @Test
    void deploymentRequiresApproval() {
        SandboxDecision decision = service.evaluate(new SandboxOperation("run-1", SandboxOperationType.DEPLOYMENT, "demo-prod"));

        assertThat(decision.decision()).isEqualTo(SandboxDecisionType.REQUIRE_APPROVAL);
    }
}
