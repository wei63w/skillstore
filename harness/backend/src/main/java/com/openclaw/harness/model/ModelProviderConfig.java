package com.openclaw.harness.model;

public record ModelProviderConfig(
        String providerKey,
        boolean enabled,
        int priority,
        String modelName,
        String endpoint,
        String apiKeyEnv,
        int timeoutSeconds,
        int maxRetries
) {
    public ModelProviderConfig {
        if (providerKey == null || providerKey.isBlank()) {
            throw new IllegalArgumentException("模型供应商标识不能为空");
        }
        if (enabled && (modelName == null || modelName.isBlank())) {
            throw new IllegalArgumentException("启用的模型供应商必须配置模型名称");
        }
        if (enabled && (apiKeyEnv == null || apiKeyEnv.isBlank()) && !"stub".equals(providerKey) && !"fake".equals(providerKey)) {
            throw new IllegalArgumentException("启用的真实模型供应商必须配置密钥环境变量名");
        }
        if (apiKeyEnv != null && (apiKeyEnv.contains("sk-") || apiKeyEnv.contains("secret"))) {
            throw new IllegalArgumentException("配置中只能保存环境变量名，禁止保存真实密钥");
        }
    }

    public ModelProviderConfig redacted() {
        return new ModelProviderConfig(providerKey, enabled, priority, modelName, endpoint,
                apiKeyEnv == null || apiKeyEnv.isBlank() ? "" : apiKeyEnv, timeoutSeconds, maxRetries);
    }
}
