package com.openclaw.harness.sandbox;

import com.openclaw.harness.gates.RiskLevel;

public record SandboxDecision(
        String decisionId,
        String runId,
        SandboxOperationType operationType,
        String target,
        RiskLevel riskLevel,
        SandboxDecisionType decision,
        String reason
) {
}
