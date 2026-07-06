package com.openclaw.skillstore.skill;

import com.openclaw.skillstore.common.DemoStoreState;

public final class SkillListingMapper {

    private SkillListingMapper() {
    }

    public static SkillListingResponse toResponse(DemoStoreState.SkillListing skill) {
        return new SkillListingResponse(
                skill.id(),
                skill.name(),
                skill.summary(),
                skill.category(),
                skill.pricingMode(),
                skill.priceCents(),
                skill.auditStatus().name(),
                skill.reviewReason());
    }
}
