package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class ConfiguredCodeModelProviderRegistryTest {

    @Test
    void selectsEnabledProviderOnly() {
        CodeModelProviderRegistry base = new CodeModelProviderRegistry(List.of(new StubCodeModelProvider(), new HttpCodeModelProvider()));
        ModelProviderSettings settings = new ModelProviderSettings(List.of(
                new ModelProviderConfig("stub", true, 10, "stub", "local://stub", "", 30, 0),
                new ModelProviderConfig("fake", false, 20, "fake", "mock://fake", "", 30, 0)
        ));
        ConfiguredCodeModelProviderRegistry registry = new ConfiguredCodeModelProviderRegistry(base, settings);

        assertThat(registry.get("stub").providerKey()).isEqualTo("stub");
        assertThatThrownBy(() -> registry.get("fake")).isInstanceOf(IllegalArgumentException.class);
    }
}
