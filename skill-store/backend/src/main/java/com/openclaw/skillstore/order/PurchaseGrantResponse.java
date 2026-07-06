package com.openclaw.skillstore.order;

public record PurchaseGrantResponse(String id, String skillId, String orderId, String downloadToken) {
}
