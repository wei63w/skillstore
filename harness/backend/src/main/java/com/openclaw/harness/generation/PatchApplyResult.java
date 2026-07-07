package com.openclaw.harness.generation;

import java.util.List;

public record PatchApplyResult(
        String requestId,
        GenerationMode mode,
        boolean applied,
        List<String> changedFiles,
        List<String> blockedReasons,
        String reportPath
) {
}
