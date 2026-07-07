package com.openclaw.harness.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ModelProviderConfigTest {

    @Test
    void rejectsPlaintextSecretInConfig() {
        assertThatThrownBy(() -> new ModelProviderConfig("openai", true, 1, "model", "https://example.com", "sk-secret", 60, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void exposesOnlySecretReference() {
        ModelProviderConfig config = new ModelProviderConfig("openai", true, 1, "model", "https://example.com", "OPENAI_API_KEY", 60, 1);

        assertThat(config.redacted().apiKeyEnv()).isEqualTo("OPENAI_API_KEY");
    }
}
