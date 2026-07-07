package com.openclaw.harness.pipeline;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class RepairLoopService {

    private final AtomicLong sequence = new AtomicLong(1);

    public RepairLoopDecision decide(String runId, int attemptNumber, PipelineStageType failureStage, String failureSummary) {
        boolean repairable = attemptNumber < 3 && failureStage != PipelineStageType.DEPLOY_DRY_RUN;
        return new RepairLoopDecision("repair-%03d".formatted(sequence.getAndIncrement()), runId, attemptNumber,
                failureStage, failureSummary, repairable, repairable ? "RETRY_GENERATION" : "OPEN_HUMAN_GATE");
    }
}
