package com.openclaw.harness.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SpecKitWorkflowServiceTest {

    private final WorkflowStateStore stateStore = new WorkflowStateStore();
    private final PhaseExecutionRecorder recorder = new PhaseExecutionRecorder(stateStore);
    private final SpecKitWorkflowService service = new SpecKitWorkflowService(
            new SpecKitArtifactValidator(),
            recorder,
            stateStore
    );

    @Test
    void startsWorkflowAndAdvancesToImplementWhenArtifactsExist() throws Exception {
        Path workspace = Files.createTempDirectory("workflow-service-");
        createCompleteFeature(workspace.resolve("specs/demo"));

        DevelopmentTask task = service.start("创建演示功能", workspace, "specs/demo");

        assertThat(task.currentStage()).isEqualTo(SpecKitStage.IMPLEMENT);
        assertThat(task.status()).isEqualTo(WorkflowStatus.RUNNING);
        assertThat(service.phases(task.taskId()))
                .extracting(PhaseExecution::stage)
                .containsExactly(SpecKitStage.SPECIFY, SpecKitStage.CLARIFY, SpecKitStage.CHECKLIST, SpecKitStage.PLAN, SpecKitStage.TASKS);
    }

    @Test
    void blocksWorkflowWhenRequiredArtifactIsMissing() throws Exception {
        Path workspace = Files.createTempDirectory("workflow-service-missing-");
        Path feature = workspace.resolve("specs/demo");
        Files.createDirectories(feature.resolve("checklists"));
        Files.writeString(feature.resolve("spec.md"), "# Spec");

        DevelopmentTask task = service.start("创建演示功能", workspace, "specs/demo");

        assertThat(task.status()).isEqualTo(WorkflowStatus.FAILED);
        assertThat(service.phases(task.taskId()).getLast().status()).isEqualTo(PhaseStatus.BLOCKED);
    }

    private void createCompleteFeature(Path feature) throws Exception {
        Files.createDirectories(feature.resolve("checklists"));
        Files.createDirectories(feature.resolve("contracts"));
        Files.writeString(feature.resolve("spec.md"), "# Spec");
        Files.writeString(feature.resolve("plan.md"), "# Plan");
        Files.writeString(feature.resolve("research.md"), "# Research");
        Files.writeString(feature.resolve("data-model.md"), "# Data");
        Files.writeString(feature.resolve("quickstart.md"), "# Quickstart");
        Files.writeString(feature.resolve("tasks.md"), "# Tasks");
    }
}
