package com.openclaw.harness.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
class GitSubmitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void submitsLocalCommitWithoutPush() throws Exception {
        Path repo = Files.createTempDirectory("git-submit-api-");
        new ProcessBuilder(List.of("git", "init")).directory(repo.toFile()).start().waitFor();
        new ProcessBuilder(List.of("git", "config", "user.email", "test@example.com")).directory(repo.toFile()).start().waitFor();
        new ProcessBuilder(List.of("git", "config", "user.name", "Harness Test")).directory(repo.toFile()).start().waitFor();
        Files.writeString(repo.resolve("README.md"), "hello");
        String body = """
                {"workspace":"%s","commitMessage":"[harness] 新增：接口提交验证","push":false}
                """.formatted(repo.toString().replace("\\", "\\\\"));

        mockMvc.perform(post("/api/harness/workflows/{taskId}/git-submit", "task-api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pushStatus").value("NOT_STARTED"));
    }
}
