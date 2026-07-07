package com.openclaw.harness.executors;

import java.util.List;

public enum TestProfile {
    UNIT(List.of("mvn", "test")),
    INTEGRATION(List.of("mvn", "test")),
    FRONTEND(List.of("npm", "test")),
    BUILD(List.of("mvn", "verify")),
    SECURITY(List.of("mvn", "verify")),
    ALL(List.of("mvn", "verify"));

    private final List<String> defaultCommand;

    TestProfile(List<String> defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    public List<String> defaultCommand() {
        return defaultCommand;
    }
}
