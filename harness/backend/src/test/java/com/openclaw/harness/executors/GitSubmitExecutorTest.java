package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskClassifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class GitSubmitExecutorTest {

    @Test
    void createsLocalCommitInTemporaryRepositoryWithoutPush() throws Exception {
        Path repo = Files.createTempDirectory("git-submit-");
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.execute(repo, repo, List.of("git", "init"));
        commandExecutor.execute(repo, repo, List.of("git", "config", "user.email", "test@example.com"));
        commandExecutor.execute(repo, repo, List.of("git", "config", "user.name", "Harness Test"));
        Files.writeString(repo.resolve("README.md"), "hello");

        GitDeliveryRecord record = new GitSubmitExecutor(
                commandExecutor,
                new CommitMessageValidator(),
                new PreCommitRiskScanner(new RiskClassifier())
        ).submit(repo, "task-git", "[harness] 新增：临时提交验证", false);

        assertThat(record.pushStatus()).isEqualTo(GitPushStatus.NOT_STARTED);
        assertThat(record.commitHash()).isNotBlank();
    }
}
