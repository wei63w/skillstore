package com.openclaw.harness.sandbox;

import com.openclaw.harness.gates.RiskLevel;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class SandboxPolicyService {

    private final AtomicLong sequence = new AtomicLong(1);

    public SandboxDecision evaluate(SandboxOperation operation) {
        String target = operation.target() == null ? "" : operation.target().toLowerCase(Locale.ROOT);
        if (target.contains("..") || target.contains(".env") || target.contains("system32")) {
            return decision(operation, RiskLevel.CRITICAL, SandboxDecisionType.DENY, "目标路径或内容越权");
        }
        if (operation.operationType() == SandboxOperationType.CREDENTIAL) {
            return decision(operation, RiskLevel.CRITICAL, SandboxDecisionType.REQUIRE_APPROVAL, "凭据访问需要人工审批");
        }
        if (operation.operationType() == SandboxOperationType.DEPLOYMENT || target.contains("prod") || target.contains("port")) {
            return decision(operation, RiskLevel.HIGH, SandboxDecisionType.REQUIRE_APPROVAL, "部署或端口操作需要人工审批");
        }
        return decision(operation, RiskLevel.MEDIUM, SandboxDecisionType.ALLOW, "低风险受控操作");
    }

    private SandboxDecision decision(SandboxOperation operation, RiskLevel riskLevel, SandboxDecisionType decision, String reason) {
        return new SandboxDecision("sandbox-%03d".formatted(sequence.getAndIncrement()), operation.runId(),
                operation.operationType(), operation.target(), riskLevel, decision, reason);
    }
}
