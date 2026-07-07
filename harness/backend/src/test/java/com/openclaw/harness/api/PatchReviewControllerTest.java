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
class PatchReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsPatchReview() throws Exception {
        Path workspace = Files.createTempDirectory("review-api-");
        Path feature = createFeature(workspace.resolve("specs/demo"));
        String body = """
                {"requestId":"attempt-api","taskId":"T001","featureDirectory":"%s","workspaceRoot":"%s","modelProvider":"stub"}
                """.formatted(escape(feature), escape(workspace));

        mockMvc.perform(post("/api/harness/code-generation/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.decision").value("APPROVED"));
    }

    @Test
    void createsPatchReviewWithFakeProvider() throws Exception {
        Path workspace = Files.createTempDirectory("review-api-fake-");
        Path feature = createFeature(workspace.resolve("specs/demo-fake"));
        String body = """
                {"requestId":"attempt-fake","taskId":"T001","featureDirectory":"%s","workspaceRoot":"%s","modelProvider":"fake"}
                """.formatted(escape(feature), escape(workspace));

        mockMvc.perform(post("/api/harness/code-generation/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.decision").value("APPROVED"));
    }

    private Path createFeature(Path feature) throws Exception {
        Files.createDirectories(feature.resolve("contracts"));
        Files.writeString(feature.resolve("spec.md"), "# Spec\n");
        Files.writeString(feature.resolve("plan.md"), "# Plan\n");
        Files.writeString(feature.resolve("tasks.md"), "- [ ] T001 demo\n");
        return feature;
    }

    private String escape(Path path) {
        return path.toString().replace("\\", "\\\\");
    }
}
