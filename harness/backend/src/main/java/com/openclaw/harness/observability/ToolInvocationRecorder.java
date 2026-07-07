package com.openclaw.harness.observability;

import com.openclaw.harness.executors.CommandResult;
import com.openclaw.harness.executors.ToolInvocation;
import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ToolInvocationRecorder {

    private final AtomicLong sequence = new AtomicLong(1);
    private final ConcurrentHashMap<String, List<ToolInvocation>> invocations = new ConcurrentHashMap<>();
    private final WorkflowStateStore stateStore;

    public ToolInvocationRecorder(WorkflowStateStore stateStore) {
        this.stateStore = stateStore;
    }

    public ToolInvocation record(String taskId, String phaseId, String toolKey, RiskLevel riskLevel, CommandResult result) {
        String invocationId = "tool-%03d".formatted(sequence.getAndIncrement());
        ToolInvocation invocation = new ToolInvocation(
                invocationId,
                taskId,
                phaseId,
                toolKey,
                result.workingDirectory(),
                String.join(" ", result.command()),
                riskLevel,
                result.exitCode(),
                runtimePath(taskId).resolve(invocationId + "-stdout.log").toString(),
                runtimePath(taskId).resolve(invocationId + "-stderr.log").toString(),
                result.elapsedMs(),
                Instant.now()
        );
        invocations.computeIfAbsent(taskId, ignored -> new ArrayList<>()).add(invocation);
        stateStore.write(runtimePath(taskId).resolve(invocationId + ".json"), invocation);
        return invocation;
    }

    public List<ToolInvocation> findByTaskId(String taskId) {
        return List.copyOf(invocations.getOrDefault(taskId, List.of()));
    }

    private Path runtimePath(String taskId) {
        return Path.of("harness", "runtime", "logs", taskId);
    }
}
