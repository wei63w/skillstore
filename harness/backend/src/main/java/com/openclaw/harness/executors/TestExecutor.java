package com.openclaw.harness.executors;

import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class TestExecutor {

    private final CommandExecutor commandExecutor;

    public TestExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public CommandResult run(Path workspaceRoot, Path workingDirectory, TestProfile profile) {
        return commandExecutor.execute(workspaceRoot, workingDirectory, profile.defaultCommand());
    }
}
