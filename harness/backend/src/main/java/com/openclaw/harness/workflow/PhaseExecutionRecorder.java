package com.openclaw.harness.workflow;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PhaseExecutionRecorder {

    private final AtomicLong sequence = new AtomicLong(1);
    private final ConcurrentHashMap<String, List<PhaseExecution>> executions = new ConcurrentHashMap<>();
    private final WorkflowStateStore stateStore;

    public PhaseExecutionRecorder(WorkflowStateStore stateStore) {
        this.stateStore = stateStore;
    }

    public PhaseExecution record(
            DevelopmentTask task,
            SpecKitStage stage,
            PhaseStatus status,
            String inputSummary,
            List<String> outputPaths,
            Instant startedAt,
            String errorSummary
    ) {
        Instant finishedAt = Instant.now();
        PhaseExecution execution = new PhaseExecution(
                "phase-%03d".formatted(sequence.getAndIncrement()),
                task.taskId(),
                stage,
                status,
                inputSummary,
                List.copyOf(outputPaths),
                startedAt,
                finishedAt,
                Duration.between(startedAt, finishedAt).toMillis(),
                errorSummary
        );
        executions.computeIfAbsent(task.taskId(), ignored -> new ArrayList<>()).add(execution);
        stateStore.write(runtimePath(task.taskId()).resolve(execution.phaseId() + ".json"), execution);
        return execution;
    }

    public List<PhaseExecution> findByTaskId(String taskId) {
        return List.copyOf(executions.getOrDefault(taskId, List.of()));
    }

    private Path runtimePath(String taskId) {
        return Path.of("harness", "runtime", "tasks", taskId, "phases");
    }
}
