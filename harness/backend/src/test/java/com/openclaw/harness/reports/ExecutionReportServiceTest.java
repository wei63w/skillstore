package com.openclaw.harness.reports;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExecutionReportServiceTest {

    @Test
    void generatesReportPathForTask() {
        ExecutionReportService service = new ExecutionReportService();

        ExecutionReport report = service.generate("task-001");

        assertThat(report.taskId()).isEqualTo("task-001");
        assertThat(report.reportPath()).isEqualTo("harness/runtime/reports/task-001-report.md");
    }
}
