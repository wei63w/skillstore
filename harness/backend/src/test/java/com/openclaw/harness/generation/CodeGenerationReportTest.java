package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.executors.CodeGenerationExecutor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodeGenerationReportTest {

    @Test
    void generationWritesPersistentReport() throws Exception {
        Path workspace = Files.createTempDirectory("codegen-report-");
        Path feature = createFeature(workspace.resolve("specs/demo-codegen"));
        Files.createDirectories(workspace.resolve("skill-store"));
        Files.writeString(workspace.resolve("skill-store/README.md"), "# Skill Store\n");

        var result = new CodeGenerationExecutor().generate(new CodeGenerationRequest(
                "report-test",
                "task-report",
                feature,
                workspace,
                List.of("skill-store/backend", "skill-store/frontend", "skill-store/README.md"),
                GenerationMode.DRY_RUN,
                "stub"
        ));

        assertThat(result.reportPath()).isEqualTo("harness/runtime/reports/report-test-code-generation.json");
        assertThat(Path.of(result.reportPath())).exists();
    }

    private Path createFeature(Path feature) throws Exception {
        Files.createDirectories(feature.resolve("contracts"));
        Files.writeString(feature.resolve("spec.md"), "# Spec\n");
        Files.writeString(feature.resolve("plan.md"), "# Plan\n");
        Files.writeString(feature.resolve("tasks.md"), "- [ ] T001 生成商城代码\n");
        return feature;
    }
}
