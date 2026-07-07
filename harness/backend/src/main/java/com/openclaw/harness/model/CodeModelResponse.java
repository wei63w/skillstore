package com.openclaw.harness.model;

import com.openclaw.harness.generation.PatchPlan;
import java.util.List;

public record CodeModelResponse(
        boolean successful,
        PatchPlan patchPlan,
        List<String> blockedReasons
) {
}
