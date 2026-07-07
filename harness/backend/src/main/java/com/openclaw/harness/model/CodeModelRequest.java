package com.openclaw.harness.model;

import com.openclaw.harness.generation.GenerationContext;
import java.util.List;

public record CodeModelRequest(
        String providerKey,
        String objective,
        GenerationContext context,
        List<String> constraints
) {
}
