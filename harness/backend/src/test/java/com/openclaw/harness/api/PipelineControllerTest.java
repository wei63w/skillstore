package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class PipelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void startsDryRunPipeline() throws Exception {
        Path workspace = Files.createTempDirectory("pipeline-api-");
        String body = """
                {"runId":"run-api","featureDirectory":"%s","workspaceRoot":"%s","dryRun":true}
                """.formatted(escape(workspace), escape(workspace));

        mockMvc.perform(post("/api/harness/pipelines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.runId").value("run-api"))
                .andExpect(jsonPath("$.data.status").value("WAITING_FOR_APPROVAL"));

        mockMvc.perform(get("/api/harness/pipelines/run-api")
                        .param("workspaceRoot", escape(workspace)))
                .andExpect(status().isOk());
    }

    private String escape(Path path) {
        return path.toString().replace("\\", "\\\\");
    }
}
