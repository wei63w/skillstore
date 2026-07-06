package com.openclaw.harness.gates;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HumanGateServiceTest {

    @Test
    void createsOpenGateForHighRiskActionAndStoresDecision() {
        HumanGateService service = new HumanGateService();

        HumanGate gate = service.openGate("task-001", RiskLevel.HIGH, "端口开放", "是否允许开放端口？", java.util.List.of("approve", "reject"));

        assertThat(gate.status()).isEqualTo(HumanGateStatus.OPEN);
        HumanGate decided = service.decide(gate.gateId(), HumanGateStatus.APPROVED, "engineer", "允许演示环境");
        assertThat(decided.status()).isEqualTo(HumanGateStatus.APPROVED);
        assertThat(decided.decision()).isEqualTo("engineer: 允许演示环境");
    }
}
