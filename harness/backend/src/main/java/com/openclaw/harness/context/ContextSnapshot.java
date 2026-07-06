package com.openclaw.harness.context;

import java.time.Instant;
import java.util.List;

public record ContextSnapshot(
        String snapshotId,
        String taskId,
        String phase,
        String summary,
        List<String> sourceRefs,
        Instant createdAt
) {
}
