package com.openclaw.harness.generation;

import java.nio.file.Path;
import java.util.List;

public record CodeGenerationRequest(
        String requestId,
        String taskId,
        Path featureDirectory,
        Path workspaceRoot,
        List<String> allowedRoots,
        GenerationMode mode,
        String modelProvider
) {
}
