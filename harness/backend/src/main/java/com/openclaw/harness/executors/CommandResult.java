package com.openclaw.harness.executors;

import java.time.Instant;
import java.util.List;

public record CommandResult(
        List<String> command,
        String workingDirectory,
        int exitCode,
        String stdout,
        String stderr,
        long elapsedMs,
        Instant startedAt
) {
    public boolean successful() {
        return exitCode == 0;
    }
}
