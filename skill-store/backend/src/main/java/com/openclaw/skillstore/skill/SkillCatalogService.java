package com.openclaw.skillstore.skill;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SkillCatalogService {

    public List<SkillSummary> featuredSkills() {
        return List.of(new SkillSummary("skill-demo-001", "PromptOps Starter", "automation", PricingMode.FREE));
    }

    public SkillSummary create(CreateSkillRequest request) {
        return new SkillSummary("skill-draft-001", request.name(), request.category(), PricingMode.FREE);
    }
}
