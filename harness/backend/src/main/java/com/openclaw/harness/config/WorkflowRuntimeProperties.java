package com.openclaw.harness.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "harness.workflow")
public record WorkflowRuntimeProperties(
        String workspaceRoot,
        int maxRetryAttempts,
        int maxConcurrentPipelines,
        long resumeTargetMillis
) {
    public WorkflowRuntimeProperties {
        if (workspaceRoot == null || workspaceRoot.isBlank()) {
            workspaceRoot = ".";
        }
        if (maxRetryAttempts <= 0) {
            maxRetryAttempts = 3;
        }
        if (maxConcurrentPipelines <= 0) {
            maxConcurrentPipelines = 3;
        }
        if (resumeTargetMillis <= 0) {
            resumeTargetMillis = 30_000;
        }
    }
}
