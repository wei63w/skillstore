package com.openclaw.harness.deployment;

public record DeploymentPlan(
        String planId,
        boolean dryRun,
        String target,
        String rollbackPlan,
        boolean requiresApproval
) {
}
