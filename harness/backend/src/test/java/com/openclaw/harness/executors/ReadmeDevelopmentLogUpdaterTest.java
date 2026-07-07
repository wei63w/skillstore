package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ReadmeDevelopmentLogUpdaterTest {

    @Test
    void prependsDevelopmentRecord() throws Exception {
        Path readme = Files.createTempFile("README", ".md");
        Files.writeString(readme, """
                | 日期 | 类型 | 范围 | 说明 | 验证结果 | GitHub 状态 |
                |------|------|------|------|----------|-------------|
                | old | old | old | old | old | old |
                """);

        new ReadmeDevelopmentLogUpdater().prependRecord(readme, "实现", "Harness", "说明", "通过", "已提交");

        assertThat(Files.readString(readme)).contains("| 实现 | Harness | 说明 | 通过 | 已提交 |");
    }
}
