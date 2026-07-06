package com.openclaw.harness.gates;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HumanGateStatus {
    OPEN("open"),
    APPROVED("approved"),
    REJECTED("rejected"),
    CANCELLED("cancelled");

    private final String value;

    HumanGateStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
