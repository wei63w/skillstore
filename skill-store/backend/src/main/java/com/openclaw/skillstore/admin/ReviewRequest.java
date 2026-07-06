package com.openclaw.skillstore.admin;

import jakarta.validation.constraints.NotNull;

public record ReviewRequest(@NotNull ReviewDecision decision, String reason) {
}
