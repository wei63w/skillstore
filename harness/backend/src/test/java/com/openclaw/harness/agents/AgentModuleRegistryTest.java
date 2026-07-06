package com.openclaw.harness.agents;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AgentModuleRegistryTest {

    @Test
    void defaultRegistryContainsAllCoreAgentModules() {
        AgentModuleRegistry registry = new AgentModuleRegistry(DefaultAgentModules.create());

        assertThat(registry.listModules())
                .extracting(AgentModuleDescriptor::moduleKey)
                .containsExactly("planning", "coding", "testing", "build-deploy", "review");
    }

    @Test
    void moduleResponsibilitiesDoNotContainSkillStoreBusinessRules() {
        AgentModuleRegistry registry = new AgentModuleRegistry(DefaultAgentModules.create());

        assertThat(registry.listModules())
                .allSatisfy(module -> assertThat(module.responsibility()).doesNotContain("商品", "订单", "支付分账"));
    }
}
