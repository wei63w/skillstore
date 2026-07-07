package com.openclaw.harness.executors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutor {

    public CommandResult execute(Path workspaceRoot, Path workingDirectory, List<String> command) {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("命令不能为空");
        }
        Path root = workspaceRoot.toAbsolutePath().normalize();
        Path workDir = workingDirectory.toAbsolutePath().normalize();
        if (!workDir.startsWith(root)) {
            throw new IllegalArgumentException("工作目录必须位于受控工作区内: " + workDir);
        }
        Instant startedAt = Instant.now();
        try {
            Process process = new ProcessBuilder(command)
                    .directory(workDir.toFile())
                    .redirectErrorStream(false)
                    .start();
            String stdout = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String stderr = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            return new CommandResult(command, workDir.toString(), exitCode, stdout, stderr,
                    Duration.between(startedAt, Instant.now()).toMillis(), startedAt);
        } catch (IOException exception) {
            return new CommandResult(command, workDir.toString(), -1, "", exception.getMessage(),
                    Duration.between(startedAt, Instant.now()).toMillis(), startedAt);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return new CommandResult(command, workDir.toString(), -1, "", "命令执行被中断",
                    Duration.between(startedAt, Instant.now()).toMillis(), startedAt);
        }
    }

    public CommandResult executeInTempWorkspace(List<String> command) throws IOException {
        Path temp = Files.createTempDirectory("harness-command-");
        return execute(temp, temp, command);
    }
}
