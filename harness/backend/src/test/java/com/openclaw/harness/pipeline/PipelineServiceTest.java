package com.openclaw.harness.pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.deployment.DeploymentPlanner;
import com.openclaw.harness.deployment.DockerBuildPlanner;
import com.openclaw.harness.security.SecurityScanPlanner;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class PipelineServiceTest {

    @Test
    void dryRunCreatesAllStagesAndReport() throws Exception {
        var service = new PipelineService(new WorkflowStateStore(), new SecurityScanPlanner(), new DockerBuildPlanner(), new DeploymentPlanner());
        var workspace = Files.createTempDirectory("pipeline-service-");

        PipelineRun run = service.dryRun("run-test", workspace, workspace);

        assertThat(run.stages()).hasSize(PipelineStageType.values().length);
        assertThat(run.status()).isEqualTo(PipelineRunStatus.WAITING_FOR_APPROVAL);
    }
}
