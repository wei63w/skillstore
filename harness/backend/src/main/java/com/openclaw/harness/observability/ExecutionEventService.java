package com.openclaw.harness.observability;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ExecutionEventService {

    private final AtomicLong sequence = new AtomicLong(1);
    private final List<ExecutionEvent> events = new ArrayList<>();

    public ExecutionEvent record(String taskId, String agentType, String action, String inputRef, String outputRef, ExecutionResult result) {
        ExecutionEvent event = new ExecutionEvent(
                "step-%03d".formatted(sequence.getAndIncrement()),
                taskId,
                agentType,
                action,
                inputRef,
                outputRef,
                null,
                0,
                result,
                Instant.now()
        );
        events.add(event);
        return event;
    }

    public List<ExecutionEvent> findByTaskId(String taskId) {
        return events.stream().filter(event -> event.taskId().equals(taskId)).toList();
    }
}
