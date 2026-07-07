package com.openclaw.harness.executors;

import com.openclaw.harness.workflow.TaskChecklistItem;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerationExecutor {

    public Path generateMarkdownArtifact(Path workspaceRoot, TaskChecklistItem item, String content) {
        Path output = workspaceRoot.resolve("harness/runtime/artifacts/%s.md".formatted(item.itemId())).normalize();
        if (!output.startsWith(workspaceRoot.normalize())) {
            throw new IllegalArgumentException("生成产物必须位于工作区内");
        }
        try {
            Files.createDirectories(output.getParent());
            Files.writeString(output, "# " + item.itemId() + "\n\n" + content + "\n");
            return output;
        } catch (IOException exception) {
            throw new IllegalStateException("生成受控产物失败: " + output, exception);
        }
    }
}
