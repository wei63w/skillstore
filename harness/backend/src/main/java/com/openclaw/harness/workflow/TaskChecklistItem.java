package com.openclaw.harness.workflow;

import java.util.List;

public record TaskChecklistItem(
        String itemId,
        String description,
        boolean parallel,
        String story,
        List<String> dependencies,
        TaskChecklistStatus status,
        int sourceLine
) {
}
