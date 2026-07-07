package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void startResumeAndListPhases() throws Exception {
        Path workspace = Files.createTempDirectory("workflow-controller-");
        createCompleteFeature(workspace.resolve("specs/demo"));
        String body = """
                {"objective":"创建演示功能","workspace":"%s","featureDirectory":"specs/demo"}
                """.formatted(workspace.toString().replace("\\", "\\\\"));

        String response = mockMvc.perform(post("/api/harness/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.currentStage").value("implement"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String taskId = response.replaceAll(".*\\\"taskId\\\":\\\"([^\\\"]+)\\\".*", "$1");

        mockMvc.perform(post("/api/harness/workflows/{taskId}/resume", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nextStage").value("implement"));

        mockMvc.perform(get("/api/harness/workflows/{taskId}/phases", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(5));
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
