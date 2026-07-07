package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.generation.GenerationContext;
import java.util.List;
import org.junit.jupiter.api.Test;

class StubCodeModelProviderTest {

    @Test
    void returnsBackendFrontendDocsAndTestCandidates() {
        CodeModelResponse response = new StubCodeModelProvider().generatePatch(new CodeModelRequest(
                "stub",
                "生成 Skill Store 演示变更",
                new GenerationContext("spec", "plan", List.of("T001"), List.of("contracts"), List.of()),
                List.of()
        ));

        assertThat(response.successful()).isTrue();
        assertThat(response.patchPlan().fileChanges()).extracting(change -> change.path())
                .anyMatch(path -> path.contains("backend"))
                .anyMatch(path -> path.contains("frontend"))
                .anyMatch(path -> path.contains("README.md"));
    }
}
