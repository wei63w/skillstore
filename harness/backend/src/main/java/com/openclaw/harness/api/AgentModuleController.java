package com.openclaw.harness.api;

import com.openclaw.harness.agents.AgentModuleDescriptor;
import com.openclaw.harness.agents.AgentModuleRegistry;
import com.openclaw.harness.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentModuleController {

    private final AgentModuleRegistry registry;

    public AgentModuleController(AgentModuleRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/api/harness/modules")
    public ApiResponse<List<AgentModuleDescriptor>> listModules() {
        return ApiResponse.ok(registry.listModules());
    }
}
