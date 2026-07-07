package com.openclaw.harness.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguredCodeModelProviderRegistry {

    private final CodeModelProviderRegistry providerRegistry;
    private final ModelProviderSettings settings;
    private final HttpCodeModelProvider httpCodeModelProvider;

    public ConfiguredCodeModelProviderRegistry(CodeModelProviderRegistry providerRegistry, ModelProviderSettings settings) {
        this(providerRegistry, settings, new HttpCodeModelProvider(settings, new com.fasterxml.jackson.databind.ObjectMapper(), java.net.http.HttpClient.newHttpClient()));
    }

    @Autowired
    public ConfiguredCodeModelProviderRegistry(CodeModelProviderRegistry providerRegistry, ModelProviderSettings settings, HttpCodeModelProvider httpCodeModelProvider) {
        this.providerRegistry = providerRegistry;
        this.settings = settings;
        this.httpCodeModelProvider = httpCodeModelProvider;
    }

    public CodeModelProvider get(String providerKey) {
        ModelProviderConfig config = providerKey == null || providerKey.isBlank()
                ? settings.firstEnabled()
                : settings.requireEnabled(providerKey);
        if (!"stub".equals(config.providerKey())) {
            return httpCodeModelProvider;
        }
        return providerRegistry.get(config.providerKey());
    }
}
