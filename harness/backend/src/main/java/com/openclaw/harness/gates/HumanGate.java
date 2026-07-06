package com.openclaw.harness.gates;

import java.time.Instant;
import java.util.List;

public record HumanGate(
        String gateId,
        String taskId,
        RiskLevel riskLevel,
        String topic,
        String question,
        List<String> options,
        String decision,
        HumanGateStatus status,
        Instant createdAt,
        Instant resolvedAt
) {
}
