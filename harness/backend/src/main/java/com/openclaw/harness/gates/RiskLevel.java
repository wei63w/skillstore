package com.openclaw.harness.gates;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RiskLevel {
    MEDIUM("medium"),
    HIGH("high"),
    CRITICAL("critical");

    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
