package com.openclaw.skillstore.skill;

import jakarta.validation.constraints.NotBlank;

public record CreateSkillRequest(
        @NotBlank String name,
        @NotBlank String category
) {
}
