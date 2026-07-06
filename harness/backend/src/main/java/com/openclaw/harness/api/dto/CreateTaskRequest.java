package com.openclaw.harness.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank String title,
        @NotBlank String objective,
        String requestedBy
) {
}
