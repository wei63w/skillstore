package com.openclaw.harness.reports;

import java.time.Instant;

public record TaskArtifact(
        String artifactId,
        String taskId,
        String type,
        String path,
        String checksum,
        Instant createdAt
) {
}
