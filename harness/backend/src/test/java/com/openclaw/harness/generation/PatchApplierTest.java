package com.openclaw.harness.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatchApplierTest {

    private final PatchApplier applier = new PatchApplier(new PatchPathPolicy(), new GenerationRiskScanner());

    @Test
    void dryRunDoesNotWriteFiles() throws Exception {
        Path root = Files.createTempDirectory("patch-dry-");
        PatchPlan plan = plan("skill-store/README.md", "hello");
        CodeGenerationRequest request = new CodeGenerationRequest("req-1", "task", root, root, List.of("skill-store"), GenerationMode.DRY_RUN, "stub");

        PatchApplyResult result = applier.apply(request, plan);

        assertThat(result.applied()).isFalse();
        assertThat(Files.exists(root.resolve("skill-store/README.md"))).isFalse();
    }

    @Test
    void applyWritesAllowedFilesAndBlocksUnsafeFiles() throws Exception {
        Path root = Files.createTempDirectory("patch-apply-");
        CodeGenerationRequest request = new CodeGenerationRequest("req-2", "task", root, root, List.of("skill-store"), GenerationMode.APPLY, "stub");

        PatchApplyResult ok = applier.apply(request, plan("skill-store/README.md", "hello"));
        PatchApplyResult blocked = applier.apply(request, plan("harness/backend/pom.xml", "bad"));

        assertThat(ok.applied()).isTrue();
        assertThat(Files.readString(root.resolve("skill-store/README.md"))).contains("hello");
        assertThat(blocked.applied()).isFalse();
        assertThat(blocked.blockedReasons()).isNotEmpty();
    }

    private PatchPlan plan(String path, String content) {
        return new PatchPlan("p", "T001", "summary",
                List.of(new FileChange(path, "create", content, "T001", "reason")),
                RiskLevel.MEDIUM,
                List.of());
    }
}
