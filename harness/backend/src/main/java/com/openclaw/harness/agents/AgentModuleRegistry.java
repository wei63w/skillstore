package com.openclaw.harness.agents;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AgentModuleRegistry {

    private final List<AgentModuleDescriptor> modules;

    public AgentModuleRegistry() {
        this(DefaultAgentModules.create());
    }

    public AgentModuleRegistry(List<AgentModuleDescriptor> modules) {
        this.modules = List.copyOf(modules);
    }

    public List<AgentModuleDescriptor> listModules() {
        return modules;
    }
}
