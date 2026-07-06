package com.openclaw.skillstore.api;

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
class StoreModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void moduleEndpointExposesBusinessBoundaries() throws Exception {
        mockMvc.perform(get("/api/store/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].moduleKey").value("buyer"))
                .andExpect(jsonPath("$.data[1].moduleKey").value("creator"))
                .andExpect(jsonPath("$.data[2].moduleKey").value("admin"));
    }
}
