package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openclaw.harness.gates.RiskClassifier;
import com.openclaw.harness.gates.RiskLevel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommandExecutorTest {

    private final CommandExecutor commandExecutor = new CommandExecutor();
    private final RiskClassifier riskClassifier = new RiskClassifier();

    @Test
    void commandExecutorRejectsDirectoryOutsideWorkspace() throws Exception {
        Path root = Files.createTempDirectory("workspace-root-");
        Path outside = Files.createTempDirectory("workspace-outside-");

        assertThatThrownBy(() -> commandExecutor.execute(root, outside, List.of("git", "--version")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("受控工作区");
    }

    @Test
    void riskClassifierEscalatesDangerousCommands() {
        assertThat(riskClassifier.classifyCommand(List.of("git", "push", "origin", "master"))).isEqualTo(RiskLevel.HIGH);
        assertThat(riskClassifier.classifyCommand(List.of("Remove-Item", "-Recurse", "C:\\"))).isEqualTo(RiskLevel.CRITICAL);
        assertThat(riskClassifier.classifyCommand(List.of("mvn", "test"))).isEqualTo(RiskLevel.MEDIUM);
    }

    @Test
    void riskClassifierFindsSensitiveChangePaths() {
        assertThat(riskClassifier.containsSensitiveChange(List.of("src/App.java", ".env.production"))).isTrue();
        assertThat(riskClassifier.containsSensitiveChange(List.of("README.md", "harness/backend/pom.xml"))).isFalse();
    }
}
