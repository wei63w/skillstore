package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SandboxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void evaluatesSandboxOperation() throws Exception {
        mockMvc.perform(post("/api/harness/sandbox/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"runId\":\"run-api\",\"operationType\":\"DEPLOYMENT\",\"target\":\"demo-prod\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.decision").value("REQUIRE_APPROVAL"));
    }
}
