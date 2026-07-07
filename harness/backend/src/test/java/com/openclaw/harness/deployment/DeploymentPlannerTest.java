package com.openclaw.harness.deployment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DeploymentPlannerTest {

    @Test
    void deploymentDryRunRequiresApproval() {
        DeploymentPlan plan = new DeploymentPlanner().dryRun("run-1", "demo");

        assertThat(plan.dryRun()).isTrue();
        assertThat(plan.requiresApproval()).isTrue();
    }
}
