package com.openclaw.harness.tools;

import com.openclaw.harness.gates.RiskLevel;

public record ToolPluginDescriptor(
        String pluginKey,
        String displayName,
        String capability,
        RiskLevel riskLevel,
        boolean enabled,
        boolean requiresHumanGate
) {
}
