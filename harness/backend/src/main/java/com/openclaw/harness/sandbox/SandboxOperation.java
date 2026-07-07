package com.openclaw.harness.sandbox;

public record SandboxOperation(
        String runId,
        SandboxOperationType operationType,
        String target
) {
}
