package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.workflow.TaskChecklistItem;
import com.openclaw.harness.workflow.TaskChecklistStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodeGenerationExecutorTest {

    @Test
    void writesControlledArtifactInsideWorkspace() throws Exception {
        Path workspace = Files.createTempDirectory("codegen-");
        TaskChecklistItem item = new TaskChecklistItem("T100", "生成产物", false, "US2", List.of(), TaskChecklistStatus.PENDING, 1);

        Path artifact = new CodeGenerationExecutor().generateMarkdownArtifact(workspace, item, "hello");

        assertThat(artifact).startsWith(workspace);
        assertThat(Files.readString(artifact)).contains("T100", "hello");
    }
}
