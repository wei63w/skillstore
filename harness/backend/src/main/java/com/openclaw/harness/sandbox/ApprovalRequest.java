package com.openclaw.harness.sandbox;

import com.openclaw.harness.gates.RiskLevel;
import java.time.Instant;
import java.util.List;

public record ApprovalRequest(
        String approvalId,
        String runId,
        RiskLevel riskLevel,
        String topic,
        String question,
        List<String> options,
        String status,
        String decisionBy,
        Instant decisionAt
) {
}
