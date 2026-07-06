package com.openclaw.skillstore.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(@NotBlank String skillId) {
}
