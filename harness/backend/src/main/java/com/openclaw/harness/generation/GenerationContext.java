package com.openclaw.harness.generation;

import java.util.List;

public record GenerationContext(
        String specSummary,
        String planSummary,
        List<String> taskItems,
        List<String> contractRefs,
        List<String> existingFiles
) {
}
