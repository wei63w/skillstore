package com.openclaw.harness.sandbox;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ApprovalRequestService {

    private final AtomicLong sequence = new AtomicLong(1);

    public ApprovalRequest open(String runId, RiskLevel riskLevel, String topic, String question, List<String> options) {
        return new ApprovalRequest("approval-%03d".formatted(sequence.getAndIncrement()), runId, riskLevel,
                topic, question, List.copyOf(options), "OPEN", null, null);
    }
}
