package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;
import org.junit.jupiter.api.Test;

class GenerationRiskScannerTest {

    @Test
    void detectsSecretsAndProductionConfiguration() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "modify", "password=123\nprod database", "T001", "risk")),
                RiskLevel.HIGH,
                List.of());

        assertThat(new GenerationRiskScanner().scan(plan)).hasSize(2);
    }
}
