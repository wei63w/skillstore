package com.openclaw.harness.workflow;

import java.util.Arrays;
import java.util.List;

public enum SpecKitStage {
    SPECIFY("specify", 1, List.of("spec.md")),
    CLARIFY("clarify", 2, List.of("spec.md")),
    CHECKLIST("checklist", 3, List.of("checklists")),
    PLAN("plan", 4, List.of("plan.md", "research.md", "data-model.md", "quickstart.md", "contracts")),
    TASKS("tasks", 5, List.of("tasks.md")),
    IMPLEMENT("implement", 6, List.of());

    private final String key;
    private final int order;
    private final List<String> requiredArtifacts;

    SpecKitStage(String key, int order, List<String> requiredArtifacts) {
        this.key = key;
        this.order = order;
        this.requiredArtifacts = requiredArtifacts;
    }

    public String key() {
        return key;
    }

    public int order() {
        return order;
    }

    public List<String> requiredArtifacts() {
        return requiredArtifacts;
    }

    public SpecKitStage next() {
        return Arrays.stream(values())
                .filter(stage -> stage.order == order + 1)
                .findFirst()
                .orElse(null);
    }

    public static SpecKitStage fromKey(String key) {
        return Arrays.stream(values())
                .filter(stage -> stage.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知 Spec Kit 阶段: " + key));
    }
}
