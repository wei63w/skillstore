package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class WorkflowReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generatesWorkflowReport() throws Exception {
        mockMvc.perform(post("/api/harness/workflows/{taskId}/report", "task-report-api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.taskId").value("task-report-api"))
                .andExpect(jsonPath("$.data.reportPath").exists());
    }
}
