package com.openclaw.harness.observability;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.executors.CommandResult;
import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class ToolInvocationRecorderTest {

    @Test
    void recordsToolInvocationForTask() {
        ToolInvocationRecorder recorder = new ToolInvocationRecorder(new WorkflowStateStore());

        var invocation = recorder.record("task-tool", "phase-1", "mvn", RiskLevel.MEDIUM,
                new CommandResult(List.of("mvn", "test"), ".", 0, "ok", "", 10, Instant.now()));

        assertThat(invocation.invocationId()).startsWith("tool-");
        assertThat(recorder.findByTaskId("task-tool")).hasSize(1);
    }
}
