package com.openclaw.harness.tools;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import org.junit.jupiter.api.Test;

class ToolPluginRegistryTest {

    @Test
    void highRiskPluginsRequireHumanGateByDefault() {
        ToolPluginRegistry registry = ToolPluginRegistry.defaults();

        assertThat(registry.listPlugins())
                .filteredOn(plugin -> plugin.riskLevel() == RiskLevel.HIGH || plugin.riskLevel() == RiskLevel.CRITICAL)
                .allSatisfy(plugin -> assertThat(plugin.requiresHumanGate()).isTrue());
    }
}
