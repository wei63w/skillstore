package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatchPathPolicyTest {

    private final PatchPathPolicy policy = new PatchPathPolicy();

    @Test
    void allowsOnlyConfiguredRootsAndRejectsSensitivePaths() {
        Path root = Path.of("C:/workspace").toAbsolutePath().normalize();
        assertThat(policy.allowed(root, List.of("skill-store", "docs"), "skill-store/README.md")).isTrue();
        assertThat(policy.allowed(root, List.of("skill-store"), "harness/backend/pom.xml")).isFalse();
        assertThat(policy.allowed(root, List.of("skill-store"), "../outside.txt")).isFalse();
        assertThat(policy.allowed(root, List.of("skill-store"), "skill-store/.env")).isFalse();
    }
}
