package com.openclaw.harness.reports;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.workflow.WorkflowStateStore;
import org.junit.jupiter.api.Test;

class ArtifactIndexServiceTest {

    @Test
    void indexesTaskArtifacts() {
        ArtifactIndexService service = new ArtifactIndexService(new WorkflowStateStore());

        TaskArtifact artifact = service.add("task-artifact", "testReport", "harness/runtime/reports/report.md");

        assertThat(artifact.artifactId()).startsWith("artifact-");
        assertThat(service.findByTaskId("task-artifact")).hasSize(1);
    }
}
