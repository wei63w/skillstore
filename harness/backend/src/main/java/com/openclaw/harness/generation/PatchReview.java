package com.openclaw.harness.generation;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;

public record PatchReview(
        String reviewId,
        String attemptId,
        List<String> changedFiles,
        String diffSummary,
        RiskLevel riskLevel,
        PatchReviewDecision decision,
        String reviewer,
        String reason
) {
}
