package com.openclaw.harness.gates;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class HumanGateService {

    private final AtomicLong sequence = new AtomicLong(1);
    private final ConcurrentHashMap<String, HumanGate> gates = new ConcurrentHashMap<>();

    public HumanGate openGate(String taskId, RiskLevel riskLevel, String topic, String question, List<String> options) {
        String gateId = "gate-%03d".formatted(sequence.getAndIncrement());
        HumanGate gate = new HumanGate(gateId, taskId, riskLevel, topic, question, List.copyOf(options), null, HumanGateStatus.OPEN, Instant.now(), null);
        gates.put(gateId, gate);
        return gate;
    }

    public HumanGate decide(String gateId, HumanGateStatus decision, String decidedBy, String comment) {
        HumanGate current = gates.get(gateId);
        if (current == null) {
            throw new IllegalArgumentException("门禁不存在: " + gateId);
        }
        HumanGate updated = new HumanGate(
                current.gateId(),
                current.taskId(),
                current.riskLevel(),
                current.topic(),
                current.question(),
                current.options(),
                decidedBy + ": " + (comment == null ? "" : comment),
                decision,
                current.createdAt(),
                Instant.now()
        );
        gates.put(gateId, updated);
        return updated;
    }
}
