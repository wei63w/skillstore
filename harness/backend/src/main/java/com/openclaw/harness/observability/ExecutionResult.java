package com.openclaw.harness.observability;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExecutionResult {
    SUCCESS("success"),
    FAILED("failed"),
    SKIPPED("skipped");

    private final String value;

    ExecutionResult(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
