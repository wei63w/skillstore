package com.openclaw.harness.retry;

import org.springframework.stereotype.Service;

@Service
public class RetryPolicy {

    public static final int MAX_ATTEMPTS = 3;

    public boolean canRetry(int currentAttempt) {
        return currentAttempt < MAX_ATTEMPTS;
    }

    public FailureCategory classify(String failureOutput) {
        String output = failureOutput == null ? "" : failureOutput.toLowerCase();
        if (output.contains("test")) {
            return FailureCategory.TEST;
        }
        if (output.contains("vulnerability") || output.contains("cve")) {
            return FailureCategory.SECURITY;
        }
        if (output.contains("dependency")) {
            return FailureCategory.DEPENDENCY;
        }
        if (output.contains("build") || output.contains("compile")) {
            return FailureCategory.BUILD;
        }
        return FailureCategory.UNKNOWN;
    }
}
