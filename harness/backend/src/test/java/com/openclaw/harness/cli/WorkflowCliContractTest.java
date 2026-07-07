package com.openclaw.harness.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WorkflowCliContractTest {

    @Test
    void workflowCliContractDocumentsStartAndResume() throws Exception {
        Path contract = Path.of("..", "..", "specs", "004-harness-automation", "contracts", "cli-contract.md");
        String content = Files.readString(contract);

        assertThat(content).contains("harness workflow start --objective");
        assertThat(content).contains("harness workflow resume --task-id");
        assertThat(content).contains("restoreElapsedMs");
    }
}
