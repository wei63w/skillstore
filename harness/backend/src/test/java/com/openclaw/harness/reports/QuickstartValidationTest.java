package com.openclaw.harness.reports;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class QuickstartValidationTest {

    @Test
    void quickstartIncludesRuntimeAndGateValidation() throws Exception {
        String quickstart = Files.readString(Path.of("..", "..", "specs", "001-init-agent-harness", "quickstart.md"));

        assertThat(quickstart)
                .contains("harness/runtime/tasks/")
                .contains("人工确认门禁验证")
                .contains("mvn test");
    }
}
