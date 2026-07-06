package com.openclaw.harness.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class HarnessCliContractTest {

    @Test
    void cliContractDocumentsRequiredCommands() throws Exception {
        String contract = Files.readString(Path.of("..", "..", "specs", "001-init-agent-harness", "contracts", "cli-contract.md"));

        assertThat(contract)
                .contains("harness task create")
                .contains("harness task status")
                .contains("harness gate decide")
                .contains("harness report generate");
    }
}
