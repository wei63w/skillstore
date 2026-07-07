package com.openclaw.harness.reports;

import com.openclaw.harness.workflow.PhaseExecutionRecorder;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReportService {

    private final PhaseExecutionRecorder phaseRecorder;
    private final ArtifactIndexService artifactIndexService;
    private final WorkflowStateStore stateStore;

    public DeliveryReportService(PhaseExecutionRecorder phaseRecorder, ArtifactIndexService artifactIndexService, WorkflowStateStore stateStore) {
        this.phaseRecorder = phaseRecorder;
        this.artifactIndexService = artifactIndexService;
        this.stateStore = stateStore;
    }

    public DeliveryReport generate(String taskId) {
        String reportPath = Path.of("harness", "runtime", "reports", taskId + "-delivery-report.json").toString();
        DeliveryReport report = new DeliveryReport(
                "report-" + taskId,
                taskId,
                "Agent Dev Harness 自主开发流水线交付报告",
                phaseRecorder.findByTaskId(taskId).stream()
                        .map(phase -> phase.stage().key() + ":" + phase.status().name().toLowerCase())
                        .toList(),
                artifactIndexService.findByTaskId(taskId),
                List.of("测试结果由 TestExecutor 和 CI 输出补充"),
                List.of("重试结果由 RetryOrchestrator 记录"),
                List.of("人工确认由 HumanGateService 记录"),
                "Git 状态由 GitSubmitExecutor 记录",
                List.of(),
                reportPath
        );
        stateStore.write(Path.of(reportPath), report);
        return report;
    }
}
