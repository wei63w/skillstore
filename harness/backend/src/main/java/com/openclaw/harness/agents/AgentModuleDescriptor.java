package com.openclaw.harness.agents;

import java.util.List;

public record AgentModuleDescriptor(
        String moduleKey,
        String displayName,
        String responsibility,
        List<String> inputArtifacts,
        List<String> outputArtifacts,
        AgentModuleStatus status
) {
}
