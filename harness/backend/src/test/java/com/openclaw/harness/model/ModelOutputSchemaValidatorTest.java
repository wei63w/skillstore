package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.generation.FileChange;
import com.openclaw.harness.generation.PatchPlan;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModelOutputSchemaValidatorTest {

    private final ModelOutputSchemaValidator validator = new ModelOutputSchemaValidator();

    @Test
    void acceptsCompletePatchPlan() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "modify", "hello", "T001", "reason")),
                RiskLevel.LOW, List.of("spec.md"));

        assertThat(validator.validate(plan).valid()).isTrue();
    }

    @Test
    void acceptsAddAliasFromModelOutput() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "ADD", "hello", "T001", "reason")),
                RiskLevel.LOW, List.of("spec.md"));

        assertThat(validator.validate(plan).valid()).isTrue();
    }

    @Test
    void allowsDeleteWithoutContent() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary",
                List.of(new FileChange("skill-store/README.md", "delete", null, "T001", "reason")),
                RiskLevel.LOW, List.of("spec.md"));

        assertThat(validator.validate(plan).valid()).isTrue();
    }

    @Test
    void rejectsMissingFileChanges() {
        PatchPlan plan = new PatchPlan("p", "T001", "summary", List.of(), RiskLevel.LOW, List.of("spec.md"));

        assertThat(validator.validate(plan).valid()).isFalse();
        assertThat(validator.validate(plan).errors()).anyMatch(error -> error.contains("fileChanges"));
    }
}
