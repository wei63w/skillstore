package com.openclaw.harness.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class WorkflowFoundationTest {

    private final WorkflowStateStore stateStore = new WorkflowStateStore();

    @Test
    void specKitStagesKeepStrictOrder() {
        assertThat(SpecKitStage.SPECIFY.next()).isEqualTo(SpecKitStage.CLARIFY);
        assertThat(SpecKitStage.CLARIFY.next()).isEqualTo(SpecKitStage.CHECKLIST);
        assertThat(SpecKitStage.CHECKLIST.next()).isEqualTo(SpecKitStage.PLAN);
        assertThat(SpecKitStage.PLAN.next()).isEqualTo(SpecKitStage.TASKS);
        assertThat(SpecKitStage.TASKS.next()).isEqualTo(SpecKitStage.IMPLEMENT);
        assertThat(SpecKitStage.IMPLEMENT.next()).isNull();
    }

    @Test
    void workflowStateStorePersistsAndReadsDevelopmentTask() throws Exception {
        Path tempDir = Files.createTempDirectory("workflow-state-");
        Path checkpoint = tempDir.resolve("checkpoint.json");
        DevelopmentTask task = new DevelopmentTask(
                "task-test",
                "验证状态持久化",
                "specs/004-harness-automation",
                SpecKitStage.PLAN,
                WorkflowStatus.RUNNING,
                checkpoint.toString(),
                Instant.now(),
                Instant.now()
        );

        stateStore.write(checkpoint, task);

        assertThat(stateStore.read(checkpoint, DevelopmentTask.class))
                .isPresent()
                .get()
                .extracting(DevelopmentTask::taskId, DevelopmentTask::currentStage)
                .containsExactly("task-test", SpecKitStage.PLAN);
    }
}
