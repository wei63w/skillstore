package com.openclaw.harness.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SecurityScanPlannerTest {

    @Test
    void createsSastAndDependencyCommands() {
        SecurityScanPlanner planner = new SecurityScanPlanner();

        assertThat(planner.sastCommand("skill-store")).contains("echo");
        assertThat(planner.dependencyScanCommand("skill-store")).contains("dependency:tree");
    }
}
