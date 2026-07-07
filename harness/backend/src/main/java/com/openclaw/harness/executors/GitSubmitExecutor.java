package com.openclaw.harness.executors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GitSubmitExecutor {

    private final CommandExecutor commandExecutor;
    private final CommitMessageValidator messageValidator;
    private final PreCommitRiskScanner riskScanner;

    public GitSubmitExecutor(CommandExecutor commandExecutor, CommitMessageValidator messageValidator, PreCommitRiskScanner riskScanner) {
        this.commandExecutor = commandExecutor;
        this.messageValidator = messageValidator;
        this.riskScanner = riskScanner;
    }

    public GitDeliveryRecord submit(Path workspace, String taskId, String commitMessage, boolean push) {
        messageValidator.requireValid(commitMessage);
        CommandResult status = commandExecutor.execute(workspace, workspace, List.of("git", "status", "--short"));
        List<String> changedFiles = status.stdout().lines()
                .map(line -> line.length() > 3 ? line.substring(3).trim() : line.trim())
                .filter(line -> !line.isBlank())
                .toList();
        CommandResult diff = commandExecutor.execute(workspace, workspace, List.of("git", "diff"));
        var scan = riskScanner.scan(changedFiles, diff.stdout());
        if (!scan.safe()) {
            return new GitDeliveryRecord("git-" + taskId, taskId, false, commitMessage, null, GitPushStatus.BLOCKED, scan.message(), changedFiles);
        }
        commandExecutor.execute(workspace, workspace, List.of("git", "add", "."));
        CommandResult commit = commandExecutor.execute(workspace, workspace, List.of("git", "commit", "-m", commitMessage));
        if (!commit.successful()) {
            return new GitDeliveryRecord("git-" + taskId, taskId, true, commitMessage, null, GitPushStatus.FAILED, commit.stderr(), changedFiles);
        }
        CommandResult hash = commandExecutor.execute(workspace, workspace, List.of("git", "rev-parse", "--short", "HEAD"));
        if (!push) {
            return new GitDeliveryRecord("git-" + taskId, taskId, true, commitMessage, hash.stdout().trim(), GitPushStatus.NOT_STARTED, null, changedFiles);
        }
        CommandResult pushed = commandExecutor.execute(workspace, workspace, Arrays.asList("git", "push"));
        return new GitDeliveryRecord(
                "git-" + taskId,
                taskId,
                true,
                commitMessage,
                hash.stdout().trim(),
                pushed.successful() ? GitPushStatus.PUSHED : GitPushStatus.FAILED,
                pushed.successful() ? null : pushed.stderr(),
                changedFiles
        );
    }
}
