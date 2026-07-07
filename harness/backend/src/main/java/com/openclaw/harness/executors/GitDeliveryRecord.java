package com.openclaw.harness.executors;

import java.util.List;

public record GitDeliveryRecord(
        String recordId,
        String taskId,
        boolean readmeUpdated,
        String commitMessage,
        String commitHash,
        GitPushStatus pushStatus,
        String blockedReason,
        List<String> changedFiles
) {
}
