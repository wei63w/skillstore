package com.openclaw.skillstore.modules;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoreModuleCatalogTest {

    @Test
    void catalogContainsBuyerCreatorAdminAndSharedModules() {
        StoreModuleCatalog catalog = StoreModuleCatalog.defaults();

        assertThat(catalog.listModules())
                .extracting(StoreModuleDescriptor::moduleKey)
                .containsExactly(
                        "buyer",
                        "creator",
                        "admin",
                        "skill",
                        "order",
                        "storage",
                        "audit",
                        "notification"
                );
    }

    @Test
    void modulesDoNotClaimHarnessCoreResponsibilities() {
        StoreModuleCatalog catalog = StoreModuleCatalog.defaults();

        assertThat(catalog.listModules())
                .allSatisfy(module -> assertThat(module.responsibility())
                        .doesNotContain("Agent 调度", "断点续跑", "工具插件池"));
    }
}
