package com.openclaw.harness.deployment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DockerBuildPlannerTest {

    @Test
    void createsDryRunCommand() {
        assertThat(new DockerBuildPlanner().dryRunCommand("skill-store")).contains("echo");
    }
}
