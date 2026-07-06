package com.openclaw.harness.reports;

import org.springframework.stereotype.Service;

@Service
public class ExecutionReportService {

    public ExecutionReport generate(String taskId) {
        return new ExecutionReport(taskId, "harness/runtime/reports/%s-report.md".formatted(taskId));
    }
}
