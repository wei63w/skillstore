package com.openclaw.harness.reports;

import java.util.List;

public record DeliveryReport(
        String reportId,
        String taskId,
        String summary,
        List<String> phaseResults,
        List<TaskArtifact> artifactIndex,
        List<String> testResults,
        List<String> retrySummary,
        List<String> humanGateSummary,
        String gitSummary,
        List<String> residualRisks,
        String reportPath
) {
}
