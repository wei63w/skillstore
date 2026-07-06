package com.openclaw.skillstore.skill;

public record SkillListingResponse(
        String id,
        String name,
        String summary,
        String category,
        PricingMode pricingMode,
        int priceCents,
        String auditStatus,
        String reviewReason) {
}
