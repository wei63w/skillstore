package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
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
class WorkflowExecutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void runTasksReturnsWorkflowState() throws Exception {
        String taskId = startWorkflow();

        mockMvc.perform(post("/api/harness/workflows/{taskId}/run-tasks", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.taskId").value(taskId));
    }

    private String startWorkflow() throws Exception {
        Path workspace = Files.createTempDirectory("workflow-exec-");
        createCompleteFeature(workspace.resolve("specs/demo"));
        String body = """
                {"objective":"创建演示功能","workspace":"%s","featureDirectory":"specs/demo"}
                """.formatted(workspace.toString().replace("\\", "\\\\"));
        String response = mockMvc.perform(post("/api/harness/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return response.replaceAll(".*\\\"taskId\\\":\\\"([^\\\"]+)\\\".*", "$1");
    }

    private void createCompleteFeature(Path feature) throws Exception {
        Files.createDirectories(feature.resolve("checklists"));
        Files.createDirectories(feature.resolve("contracts"));
        Files.writeString(feature.resolve("spec.md"), "# Spec");
        Files.writeString(feature.resolve("plan.md"), "# Plan");
        Files.writeString(feature.resolve("research.md"), "# Research");
        Files.writeString(feature.resolve("data-model.md"), "# Data");
        Files.writeString(feature.resolve("quickstart.md"), "# Quickstart");
        Files.writeString(feature.resolve("tasks.md"), "# Tasks");
    }
}
