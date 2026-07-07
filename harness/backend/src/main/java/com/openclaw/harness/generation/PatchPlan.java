package com.openclaw.harness.generation;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;

public record PatchPlan(
        String planId,
        String taskId,
        String summary,
        List<FileChange> fileChanges,
        RiskLevel riskLevel,
        List<String> sourceRefs
) {
}
