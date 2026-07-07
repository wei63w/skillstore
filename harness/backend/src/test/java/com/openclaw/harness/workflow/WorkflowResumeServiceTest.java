package com.openclaw.harness.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WorkflowResumeServiceTest {

    @Test
    void resumesFromPersistedCheckpointWithinTarget() throws Exception {
        WorkflowStateStore stateStore = new WorkflowStateStore();
        SpecKitWorkflowService workflowService = new SpecKitWorkflowService(
                new SpecKitArtifactValidator(),
                new PhaseExecutionRecorder(stateStore),
                stateStore
        );
        WorkflowResumeService resumeService = new WorkflowResumeService(stateStore, workflowService);
        Path workspace = Files.createTempDirectory("workflow-resume-");
        createCompleteFeature(workspace.resolve("specs/demo"));
        DevelopmentTask task = workflowService.start("创建演示功能", workspace, "specs/demo");

        ResumeResult result = resumeService.resume(task.taskId());

        assertThat(result.taskId()).isEqualTo(task.taskId());
        assertThat(result.resumedFrom()).isEqualTo(SpecKitStage.IMPLEMENT);
        assertThat(result.restoreElapsedMs()).isLessThan(30_000);
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
