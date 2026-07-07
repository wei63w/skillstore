package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.reports.DeliveryReport;
import com.openclaw.harness.reports.DeliveryReportService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowReportController {

    private final DeliveryReportService deliveryReportService;

    public WorkflowReportController(DeliveryReportService deliveryReportService) {
        this.deliveryReportService = deliveryReportService;
    }

    @PostMapping("/api/harness/workflows/{taskId}/report")
    public ApiResponse<DeliveryReport> generate(@PathVariable String taskId) {
        return ApiResponse.ok(deliveryReportService.generate(taskId));
    }
}
