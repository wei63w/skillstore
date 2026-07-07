package com.openclaw.harness.model;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModelProviderSettings {

    private final List<ModelProviderConfig> providers;

    public ModelProviderSettings() {
        this(List.of(
                new ModelProviderConfig("stub", true, 100, "local-stub", "local://stub", "", 30, 0),
                new ModelProviderConfig("openai", false, 10, "gpt-5-codex", "https://api.openai.com/v1/responses", "OPENAI_API_KEY", 120, 2),
                new ModelProviderConfig("anthropic", false, 20, "claude-code-model", "https://api.anthropic.com/v1/messages", "ANTHROPIC_API_KEY", 120, 2)
        ));
    }

    public ModelProviderSettings(List<ModelProviderConfig> providers) {
        this.providers = List.copyOf(providers);
    }

    public List<ModelProviderConfig> redactedProviders() {
        return providers.stream().map(ModelProviderConfig::redacted).toList();
    }

    public ModelProviderConfig requireEnabled(String providerKey) {
        return providers.stream()
                .filter(provider -> provider.providerKey().equals(providerKey))
                .findFirst()
                .filter(ModelProviderConfig::enabled)
                .orElseThrow(() -> new IllegalArgumentException("模型供应商未启用或不存在: " + providerKey));
    }

    public ModelProviderConfig firstEnabled() {
        return providers.stream()
                .filter(ModelProviderConfig::enabled)
                .min(Comparator.comparingInt(ModelProviderConfig::priority))
                .orElseThrow(() -> new IllegalStateException("没有启用的模型供应商"));
    }
}
