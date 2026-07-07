package com.openclaw.harness.pipeline;

import com.openclaw.harness.deployment.DeploymentPlanner;
import com.openclaw.harness.deployment.DockerBuildPlanner;
import com.openclaw.harness.security.SecurityScanPlanner;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    private final WorkflowStateStore stateStore;
    private final SecurityScanPlanner securityScanPlanner;
    private final DockerBuildPlanner dockerBuildPlanner;
    private final DeploymentPlanner deploymentPlanner;

    public PipelineService(WorkflowStateStore stateStore, SecurityScanPlanner securityScanPlanner, DockerBuildPlanner dockerBuildPlanner, DeploymentPlanner deploymentPlanner) {
        this.stateStore = stateStore;
        this.securityScanPlanner = securityScanPlanner;
        this.dockerBuildPlanner = dockerBuildPlanner;
        this.deploymentPlanner = deploymentPlanner;
    }

    public PipelineRun dryRun(String runId, Path featureDirectory, Path workspaceRoot) {
        List<PipelineStageResult> stages = new ArrayList<>();
        for (PipelineStageType type : PipelineStageType.values()) {
            stages.add(stage(runId, type, type == PipelineStageType.DEPLOY_DRY_RUN ? PipelineStageStatus.WAITING_FOR_APPROVAL : PipelineStageStatus.PASSED));
        }
        securityScanPlanner.sastCommand(workspaceRoot.toString());
        dockerBuildPlanner.dryRunCommand("skill-store");
        deploymentPlanner.dryRun(runId, workspaceRoot.toString());
        PipelineRun run = new PipelineRun(runId, featureDirectory.toString(), workspaceRoot.toString(),
                PipelineRunStatus.WAITING_FOR_APPROVAL, PipelineStageType.DEPLOY_DRY_RUN, 0,
                "harness/runtime/reports/%s-pipeline.json".formatted(runId), List.copyOf(stages));
        stateStore.write(Path.of(run.reportPath()), run);
        return run;
    }

    private PipelineStageResult stage(String runId, PipelineStageType type, PipelineStageStatus status) {
        Instant now = Instant.now();
        return new PipelineStageResult("%s-%s".formatted(runId, type.name().toLowerCase()), runId, type, status,
                now, now, "harness/runtime/artifacts/%s-%s.txt".formatted(runId, type.name().toLowerCase()), null);
    }
}
