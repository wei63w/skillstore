package com.openclaw.skillstore.order;

import jakarta.validation.constraints.NotNull;

public record PayOrderRequest(@NotNull PaymentResult result) {
}
