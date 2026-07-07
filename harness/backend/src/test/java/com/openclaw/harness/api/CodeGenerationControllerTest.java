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
class CodeGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dryRunReturnsPatchPlanWithoutApplyingFiles() throws Exception {
        Path workspace = Files.createTempDirectory("codegen-api-");
        Path feature = createFeature(workspace.resolve("specs/demo-codegen"));
        Files.createDirectories(workspace.resolve("skill-store"));
        Files.writeString(workspace.resolve("skill-store/README.md"), "# Skill Store\n");

        String body = """
                {
                  "requestId":"api-dry-run",
                  "taskId":"task-codegen",
                  "featureDirectory":"%s",
                  "workspaceRoot":"%s",
                  "mode":"dry_run",
                  "modelProvider":"stub"
                }
                """.formatted(escape(feature), escape(workspace));

        mockMvc.perform(post("/api/harness/code-generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value("api-dry-run"))
                .andExpect(jsonPath("$.data.applied").value(false))
                .andExpect(jsonPath("$.data.changedFiles[0]").value("skill-store/README.md"));
    }

    @Test
    void dryRunWithFakeProviderUsesConfiguredProviderRegistry() throws Exception {
        Path workspace = Files.createTempDirectory("codegen-api-fake-");
        Path feature = createFeature(workspace.resolve("specs/demo-codegen-fake"));
        Files.createDirectories(workspace.resolve("skill-store"));
        Files.writeString(workspace.resolve("skill-store/README.md"), "# Skill Store\n");

        String body = """
                {
                  "requestId":"api-fake-provider",
                  "taskId":"task-codegen",
                  "featureDirectory":"%s",
                  "workspaceRoot":"%s",
                  "mode":"dry_run",
                  "modelProvider":"fake"
                }
                """.formatted(escape(feature), escape(workspace));

        mockMvc.perform(post("/api/harness/code-generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestId").value("api-fake-provider"))
                .andExpect(jsonPath("$.data.blockedReasons").isEmpty());
    }

    private Path createFeature(Path feature) throws Exception {
        Files.createDirectories(feature.resolve("contracts"));
        Files.writeString(feature.resolve("spec.md"), "# Spec\n");
        Files.writeString(feature.resolve("plan.md"), "# Plan\n");
        Files.writeString(feature.resolve("tasks.md"), "- [ ] T001 生成商城代码\n");
        return feature;
    }

    private String escape(Path path) {
        return path.toString().replace("\\", "\\\\");
    }
}
