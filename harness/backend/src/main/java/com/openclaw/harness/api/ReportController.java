package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.reports.ExecutionReport;
import com.openclaw.harness.reports.ExecutionReportService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ExecutionReportService service;

    public ReportController(ExecutionReportService service) {
        this.service = service;
    }

    @PostMapping("/api/harness/tasks/{taskId}/report")
    public ApiResponse<ExecutionReport> generate(@PathVariable String taskId) {
        return ApiResponse.ok(service.generate(taskId));
    }
}
