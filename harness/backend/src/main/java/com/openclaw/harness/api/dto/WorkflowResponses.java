package com.openclaw.harness.api.dto;

import com.openclaw.harness.workflow.DevelopmentTask;
import com.openclaw.harness.workflow.PhaseExecution;
import com.openclaw.harness.workflow.ResumeResult;
import java.util.List;

public final class WorkflowResponses {

    private WorkflowResponses() {
    }

    public record WorkflowResponse(
            String taskId,
            String status,
            String currentStage,
            String checkpointPath
    ) {
        public static WorkflowResponse from(DevelopmentTask task) {
            return new WorkflowResponse(
                    task.taskId(),
                    task.status().name().toLowerCase(),
                    task.currentStage().key(),
                    task.checkpointPath()
            );
        }
    }

    public record PhaseExecutionResponse(
            String phaseId,
            String taskId,
            String stageKey,
            String status,
            List<String> outputPaths,
            long elapsedMs,
            String errorSummary
    ) {
        public static PhaseExecutionResponse from(PhaseExecution phase) {
            return new PhaseExecutionResponse(
                    phase.phaseId(),
                    phase.taskId(),
                    phase.stage().key(),
                    phase.status().name().toLowerCase(),
                    phase.outputPaths(),
                    phase.elapsedMs(),
                    phase.errorSummary()
            );
        }
    }

    public record ResumeResponse(
            String taskId,
            String resumedFrom,
            String nextStage,
            long restoreElapsedMs
    ) {
        public static ResumeResponse from(ResumeResult result) {
            return new ResumeResponse(
                    result.taskId(),
                    result.resumedFrom().key(),
                    result.nextStage().key(),
                    result.restoreElapsedMs()
            );
        }
    }
}
