package com.openclaw.harness.executors;

import com.openclaw.harness.gates.RiskLevel;
import java.time.Instant;

public record ToolInvocation(
        String invocationId,
        String taskId,
        String phaseId,
        String toolKey,
        String workingDirectory,
        String command,
        RiskLevel riskLevel,
        int exitCode,
        String stdoutPath,
        String stderrPath,
        long elapsedMs,
        Instant createdAt
) {
}
