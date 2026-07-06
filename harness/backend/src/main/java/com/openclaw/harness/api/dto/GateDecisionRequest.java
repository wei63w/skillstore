package com.openclaw.harness.api.dto;

import com.openclaw.harness.gates.HumanGateStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GateDecisionRequest(
        @NotNull HumanGateStatus decision,
        @NotBlank String decidedBy,
        String comment
) {
}
