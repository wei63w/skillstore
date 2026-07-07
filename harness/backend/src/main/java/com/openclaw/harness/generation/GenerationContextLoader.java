package com.openclaw.harness.generation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenerationContextLoader {

    public GenerationContext load(Path featureDirectory) {
        require(featureDirectory.resolve("spec.md"));
        require(featureDirectory.resolve("plan.md"));
        require(featureDirectory.resolve("tasks.md"));
        return new GenerationContext(
                readFirst(featureDirectory.resolve("spec.md")),
                readFirst(featureDirectory.resolve("plan.md")),
                readTasks(featureDirectory.resolve("tasks.md")),
                Files.exists(featureDirectory.resolve("contracts")) ? List.of(featureDirectory.resolve("contracts").toString()) : List.of(),
                List.of()
        );
    }

    private void require(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("缺少生成上下文产物: " + path);
        }
    }

    private String readFirst(Path path) {
        try {
            return Files.readString(path).lines().limit(20).reduce("", (left, right) -> left + "\n" + right).trim();
        } catch (IOException exception) {
            throw new IllegalStateException("读取生成上下文失败: " + path, exception);
        }
    }

    private List<String> readTasks(Path path) {
        try {
            return Files.readString(path).lines()
                    .filter(line -> line.startsWith("- [ ]") || line.startsWith("- [x]"))
                    .limit(20)
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("读取任务上下文失败: " + path, exception);
        }
    }
}
