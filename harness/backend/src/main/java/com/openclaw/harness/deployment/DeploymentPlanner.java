package com.openclaw.harness.deployment;

import org.springframework.stereotype.Service;

@Service
public class DeploymentPlanner {

    public DeploymentPlan dryRun(String runId, String target) {
        return new DeploymentPlan("deploy-" + runId, true, target, "保留上一稳定镜像并执行回滚脚本", true);
    }
}
