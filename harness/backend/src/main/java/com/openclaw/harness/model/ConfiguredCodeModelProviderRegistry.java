package com.openclaw.harness.model;

import org.springframework.stereotype.Service;

@Service
public class ConfiguredCodeModelProviderRegistry {

    private final CodeModelProviderRegistry providerRegistry;
    private final ModelProviderSettings settings;

    public ConfiguredCodeModelProviderRegistry(CodeModelProviderRegistry providerRegistry, ModelProviderSettings settings) {
        this.providerRegistry = providerRegistry;
        this.settings = settings;
    }

    public CodeModelProvider get(String providerKey) {
        ModelProviderConfig config = providerKey == null || providerKey.isBlank()
                ? settings.firstEnabled()
                : settings.requireEnabled(providerKey);
        return providerRegistry.get(config.providerKey());
    }
}
