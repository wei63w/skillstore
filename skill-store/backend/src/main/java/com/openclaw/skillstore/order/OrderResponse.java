package com.openclaw.skillstore.order;

public record OrderResponse(
        String id,
        String orderNo,
        String skillId,
        int amountCents,
        PaymentStatus paymentStatus,
        PurchaseGrantResponse grant) {
}
