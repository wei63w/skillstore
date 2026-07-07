package com.openclaw.harness.retry;

public record RetryAttempt(
        String retryId,
        String taskId,
        String phaseId,
        int attemptNumber,
        FailureCategory failureCategory,
        String failureSummary,
        String repairSummary,
        RetryVerificationResult verificationResult
) {
}
