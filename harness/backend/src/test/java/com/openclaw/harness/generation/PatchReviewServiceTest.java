package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.model.SchemaValidationResult;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatchReviewServiceTest {

    @Test
    void approvesValidNonCriticalPatch() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "modify", "hello", "T001", "reason")),
                RiskLevel.LOW, List.of("spec.md"));

        PatchReview review = new PatchReviewService().review("attempt-1", plan, SchemaValidationResult.pass());

        assertThat(review.decision()).isEqualTo(PatchReviewDecision.APPROVED);
        assertThat(review.changedFiles()).contains("skill-store/README.md");
    }

    @Test
    void rejectsInvalidSchema() {
        PatchReview review = new PatchReviewService().review("attempt-1", null, SchemaValidationResult.fail(List.of("bad schema")));

        assertThat(review.decision()).isEqualTo(PatchReviewDecision.REJECTED);
        assertThat(review.reason()).contains("bad schema");
    }
}
