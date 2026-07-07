package com.openclaw.harness.retry;

import com.openclaw.harness.gates.HumanGate;
import com.openclaw.harness.gates.HumanGateService;
import com.openclaw.harness.gates.RiskLevel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RetryOrchestrator {

    private final RetryPolicy retryPolicy;
    private final HumanGateService humanGateService;

    public RetryOrchestrator(RetryPolicy retryPolicy, HumanGateService humanGateService) {
        this.retryPolicy = retryPolicy;
        this.humanGateService = humanGateService;
    }

    public RetryOutcome handleFailure(String taskId, String phaseId, String failureOutput, int failedAttempts) {
        List<RetryAttempt> attempts = new ArrayList<>();
        FailureCategory category = retryPolicy.classify(failureOutput);
        for (int attempt = 1; attempt <= Math.min(failedAttempts, RetryPolicy.MAX_ATTEMPTS); attempt++) {
            attempts.add(new RetryAttempt(
                    "retry-%03d".formatted(attempt),
                    taskId,
                    phaseId,
                    attempt,
                    category,
                    failureOutput,
                    "自动修复尝试 %d".formatted(attempt),
                    RetryVerificationResult.FAILED
            ));
        }
        HumanGate gate = null;
        if (!retryPolicy.canRetry(failedAttempts)) {
            gate = humanGateService.openGate(
                    taskId,
                    RiskLevel.HIGH,
                    "自动修复失败",
                    "同一问题已自动修复三轮仍失败，是否人工介入？",
                    List.of("人工接管", "调整需求", "暂停发布")
            );
        }
        return new RetryOutcome(attempts, gate);
    }
}
