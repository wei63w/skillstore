package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class HarnessTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTaskValidatesRequiredTitleAndObjective() throws Exception {
        mockMvc.perform(post("/api/harness/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAndQueryTask() throws Exception {
        String response = mockMvc.perform(post("/api/harness/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"示例任务\",\"objective\":\"验证骨架\",\"requestedBy\":\"tester\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("created"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String taskId = response.replaceAll(".*\\\"taskId\\\":\\\"([^\\\"]+)\\\".*", "$1");

        mockMvc.perform(get("/api/harness/tasks/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.taskId").value(taskId));
    }
}
