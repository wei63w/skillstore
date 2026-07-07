package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class CodeModelProviderRegistryTest {

    @Test
    void resolvesRegisteredProviderByKey() {
        CodeModelProviderRegistry registry = new CodeModelProviderRegistry(List.of(new StubCodeModelProvider()));

        assertThat(registry.get("stub").providerKey()).isEqualTo("stub");
        assertThatThrownBy(() -> registry.get("missing")).isInstanceOf(IllegalArgumentException.class);
    }
}
