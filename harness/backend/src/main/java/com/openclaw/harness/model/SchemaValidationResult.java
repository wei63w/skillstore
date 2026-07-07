package com.openclaw.harness.model;

import java.util.List;

public record SchemaValidationResult(boolean valid, List<String> errors) {

    public static SchemaValidationResult pass() {
        return new SchemaValidationResult(true, List.of());
    }

    public static SchemaValidationResult fail(List<String> errors) {
        return new SchemaValidationResult(false, List.copyOf(errors));
    }
}
