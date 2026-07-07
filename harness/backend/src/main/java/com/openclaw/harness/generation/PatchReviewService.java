package com.openclaw.harness.generation;

import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.model.SchemaValidationResult;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PatchReviewService {

    private final AtomicLong sequence = new AtomicLong(1);

    public PatchReview review(String attemptId, PatchPlan plan, SchemaValidationResult validation) {
        if (!validation.valid()) {
            return new PatchReview(nextId(), attemptId, List.of(), "Schema 校验失败", RiskLevel.HIGH,
                    PatchReviewDecision.REJECTED, "agent", String.join("; ", validation.errors()));
        }
        List<String> changedFiles = plan.fileChanges().stream().map(FileChange::path).toList();
        PatchReviewDecision decision = plan.riskLevel() == RiskLevel.CRITICAL
                ? PatchReviewDecision.REQUIRES_HUMAN_GATE
                : PatchReviewDecision.APPROVED;
        return new PatchReview(nextId(), attemptId, changedFiles,
                "%d 个文件待变更".formatted(changedFiles.size()),
                plan.riskLevel(), decision, "agent", "Schema 校验通过");
    }

    private String nextId() {
        return "review-%03d".formatted(sequence.getAndIncrement());
    }
}
