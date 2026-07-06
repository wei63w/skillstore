package com.openclaw.harness.api.dto;

import com.openclaw.harness.gates.HumanGate;
import com.openclaw.harness.gates.HumanGateStatus;
import com.openclaw.harness.gates.RiskLevel;

public record HumanGateResponse(String gateId, HumanGateStatus status, RiskLevel riskLevel, String topic) {

    public static HumanGateResponse from(HumanGate gate) {
        return new HumanGateResponse(gate.gateId(), gate.status(), gate.riskLevel(), gate.topic());
    }
}
