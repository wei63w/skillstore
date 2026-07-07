package com.openclaw.harness.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CodeModelProviderRegistry {

    private final Map<String, CodeModelProvider> providers;

    public CodeModelProviderRegistry(List<CodeModelProvider> providers) {
        this.providers = providers.stream().collect(Collectors.toMap(CodeModelProvider::providerKey, Function.identity()));
    }

    public CodeModelProvider get(String providerKey) {
        CodeModelProvider provider = providers.get(providerKey);
        if (provider == null) {
            throw new IllegalArgumentException("代码模型供应商不存在: " + providerKey);
        }
        return provider;
    }
}
