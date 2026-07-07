package com.openclaw.harness.executors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ReadmeDevelopmentLogUpdater {

    public void prependRecord(Path readme, String type, String scope, String description, String verification, String githubStatus) {
        try {
            String content = Files.readString(readme);
            String row = "| %s | %s | %s | %s | %s | %s |%n".formatted(
                    LocalDate.now(), type, scope, description, verification, githubStatus);
            String marker = "|------|------|------|------|----------|-------------|";
            int markerIndex = content.indexOf(marker);
            if (markerIndex < 0) {
                throw new IllegalArgumentException("README 缺少开发记录表格");
            }
            int insertAt = content.indexOf('\n', markerIndex);
            String updated = content.substring(0, insertAt + 1) + row + content.substring(insertAt + 1);
            Files.writeString(readme, updated);
        } catch (IOException exception) {
            throw new IllegalStateException("更新 README 开发记录失败: " + readme, exception);
        }
    }
}
