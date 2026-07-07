package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GenerationContextLoaderTest {

    @Test
    void loadsRequiredSpecKitArtifacts() throws Exception {
        Path feature = Files.createTempDirectory("feature-");
        Files.writeString(feature.resolve("spec.md"), "# Spec");
        Files.writeString(feature.resolve("plan.md"), "# Plan");
        Files.writeString(feature.resolve("tasks.md"), "- [ ] T001 Task in `README.md`");
        Files.createDirectories(feature.resolve("contracts"));

        GenerationContext context = new GenerationContextLoader().load(feature);

        assertThat(context.specSummary()).contains("Spec");
        assertThat(context.taskItems()).hasSize(1);
        assertThat(context.contractRefs()).hasSize(1);
    }

    @Test
    void rejectsMissingArtifacts() {
        assertThatThrownBy(() -> new GenerationContextLoader().load(Path.of("missing-feature")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
