package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ModelProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsProvidersWithRedactedSecretReferences() throws Exception {
        mockMvc.perform(get("/api/harness/model-providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].providerKey").value("stub"))
                .andExpect(jsonPath("$.data[?(@.providerKey == 'deepseek')].secretEnv").value("DEEPSEEK_API_KEY"))
                .andExpect(jsonPath("$.data[?(@.providerKey == 'deepseek')].message").value("缺少环境变量: DEEPSEEK_API_KEY"));
    }
}
