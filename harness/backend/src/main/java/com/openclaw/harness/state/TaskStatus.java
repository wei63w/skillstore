package com.openclaw.harness.state;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    CREATED("created"),
    RUNNING("running"),
    WAITING_HUMAN("waitingHuman"),
    FAILED("failed"),
    COMPLETED("completed"),
    ROLLED_BACK("rolledBack");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
