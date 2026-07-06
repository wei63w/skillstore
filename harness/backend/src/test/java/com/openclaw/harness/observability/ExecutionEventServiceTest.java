package com.openclaw.harness.observability;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExecutionEventServiceTest {

    @Test
    void recordsExecutionEventWithTraceableInputAndOutput() {
        ExecutionEventService service = new ExecutionEventService();

        ExecutionEvent event = service.record("task-001", "planning", "create-plan", "input.md", "output.md", ExecutionResult.SUCCESS);

        assertThat(event.stepId()).startsWith("step-");
        assertThat(service.findByTaskId("task-001")).contains(event);
        assertThat(event.inputRef()).isEqualTo("input.md");
        assertThat(event.outputRef()).isEqualTo("output.md");
    }
}
