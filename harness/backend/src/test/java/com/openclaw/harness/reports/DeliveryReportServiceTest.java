package com.openclaw.harness.reports;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.workflow.DevelopmentTask;
import com.openclaw.harness.workflow.PhaseExecutionRecorder;
import com.openclaw.harness.workflow.PhaseStatus;
import com.openclaw.harness.workflow.SpecKitStage;
import com.openclaw.harness.workflow.WorkflowStateStore;
import com.openclaw.harness.workflow.WorkflowStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class DeliveryReportServiceTest {

    @Test
    void deliveryReportIncludesPhaseAndArtifactEvidence() {
        WorkflowStateStore stateStore = new WorkflowStateStore();
        PhaseExecutionRecorder phaseRecorder = new PhaseExecutionRecorder(stateStore);
        ArtifactIndexService artifactIndexService = new ArtifactIndexService(stateStore);
        DevelopmentTask task = new DevelopmentTask("task-report", "目标", "specs/demo", SpecKitStage.IMPLEMENT,
                WorkflowStatus.RUNNING, "checkpoint.json", Instant.now(), Instant.now());
        phaseRecorder.record(task, SpecKitStage.PLAN, PhaseStatus.PASSED, "input", List.of("plan.md"), Instant.now(), null);
        artifactIndexService.add(task.taskId(), "plan", "specs/demo/plan.md");

        DeliveryReport report = new DeliveryReportService(phaseRecorder, artifactIndexService, stateStore).generate(task.taskId());

        assertThat(report.phaseResults()).contains("plan:passed");
        assertThat(report.artifactIndex()).hasSize(1);
        assertThat(report.reportPath()).contains("task-report");
    }
}
