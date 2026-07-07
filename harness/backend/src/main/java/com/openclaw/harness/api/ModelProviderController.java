package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.model.ModelProviderConfig;
import com.openclaw.harness.model.ModelProviderSettings;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelProviderController {

    private final ModelProviderSettings settings;

    public ModelProviderController(ModelProviderSettings settings) {
        this.settings = settings;
    }

    @GetMapping("/api/harness/model-providers")
    public ResponseEntity<ApiResponse<List<ModelProviderStatus>>> list() {
        List<ModelProviderStatus> providers = settings.redactedProviders().stream()
                .map(this::toStatus)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(providers));
    }

    private ModelProviderStatus toStatus(ModelProviderConfig config) {
        boolean secretRequired = config.apiKeyEnv() != null && !config.apiKeyEnv().isBlank();
        boolean configured = !secretRequired || System.getenv(config.apiKeyEnv()) != null;
        return new ModelProviderStatus(
                config.providerKey(),
                config.modelName(),
                config.endpoint(),
                config.apiKeyEnv(),
                config.enabled(),
                configured,
                configured ? "可用" : "缺少环境变量: " + config.apiKeyEnv()
        );
    }

    public record ModelProviderStatus(
            String providerKey,
            String modelName,
            String endpoint,
            String secretEnv,
            boolean enabled,
            boolean configured,
            String message
    ) {
    }
}
