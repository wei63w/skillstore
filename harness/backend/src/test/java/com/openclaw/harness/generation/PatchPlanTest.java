package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatchPlanTest {

    @Test
    void patchPlanKeepsTaskAndSourceTraceability() {
        PatchPlan plan = new PatchPlan("plan-1", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "modify", "content", "T001", "reason")),
                RiskLevel.MEDIUM,
                List.of("spec.md", "tasks.md"));

        assertThat(plan.fileChanges().getFirst().taskId()).isEqualTo("T001");
        assertThat(plan.sourceRefs()).contains("spec.md", "tasks.md");
    }
}
