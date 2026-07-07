package com.openclaw.harness.workflow;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class SpecKitWorkflowService {

    private final AtomicLong sequence = new AtomicLong(1);
    private final ConcurrentHashMap<String, DevelopmentTask> tasks = new ConcurrentHashMap<>();
    private final SpecKitArtifactValidator artifactValidator;
    private final PhaseExecutionRecorder phaseRecorder;
    private final WorkflowStateStore stateStore;

    public SpecKitWorkflowService(
            SpecKitArtifactValidator artifactValidator,
            PhaseExecutionRecorder phaseRecorder,
            WorkflowStateStore stateStore
    ) {
        this.artifactValidator = artifactValidator;
        this.phaseRecorder = phaseRecorder;
        this.stateStore = stateStore;
    }

    public DevelopmentTask start(String objective, Path workspace, String featureDirectory) {
        if (objective == null || objective.isBlank()) {
            throw new IllegalArgumentException("业务目标不能为空");
        }
        Path featurePath = workspace.resolve(featureDirectory).normalize();
        if (!featurePath.startsWith(workspace.normalize())) {
            throw new IllegalArgumentException("功能目录必须位于工作区内");
        }
        String taskId = "workflow-%03d".formatted(sequence.getAndIncrement());
        Instant now = Instant.now();
        DevelopmentTask task = new DevelopmentTask(
                taskId,
                objective,
                featureDirectory,
                SpecKitStage.SPECIFY,
                WorkflowStatus.RUNNING,
                checkpointPath(taskId).toString(),
                now,
                now
        );
        tasks.put(taskId, task);

        for (SpecKitStage stage : Arrays.asList(SpecKitStage.SPECIFY, SpecKitStage.CLARIFY, SpecKitStage.CHECKLIST, SpecKitStage.PLAN, SpecKitStage.TASKS)) {
            Instant startedAt = Instant.now();
            List<String> missing = artifactValidator.missingArtifacts(featurePath, stage);
            if (!missing.isEmpty()) {
                DevelopmentTask blocked = update(task, stage, WorkflowStatus.FAILED);
                phaseRecorder.record(blocked, stage, PhaseStatus.BLOCKED, objective, missing, startedAt,
                        "缺少阶段产物: " + String.join(", ", missing));
                persistCheckpoint(blocked);
                return blocked;
            }
            phaseRecorder.record(task, stage, PhaseStatus.PASSED, objective, stage.requiredArtifacts(), startedAt, null);
            task = update(task, stage.next() == null ? stage : stage.next(), WorkflowStatus.RUNNING);
            persistCheckpoint(task);
        }
        DevelopmentTask ready = update(task, SpecKitStage.IMPLEMENT, WorkflowStatus.RUNNING);
        persistCheckpoint(ready);
        return ready;
    }

    public List<PhaseExecution> phases(String taskId) {
        return phaseRecorder.findByTaskId(taskId);
    }

    public DevelopmentTask get(String taskId) {
        DevelopmentTask task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("流水线任务不存在: " + taskId);
        }
        return task;
    }

    void restoreInMemory(DevelopmentTask task) {
        tasks.put(task.taskId(), task);
    }

    private DevelopmentTask update(DevelopmentTask task, SpecKitStage currentStage, WorkflowStatus status) {
        DevelopmentTask updated = new DevelopmentTask(
                task.taskId(),
                task.objective(),
                task.featureDirectory(),
                currentStage,
                status,
                task.checkpointPath(),
                task.createdAt(),
                Instant.now()
        );
        tasks.put(updated.taskId(), updated);
        return updated;
    }

    private void persistCheckpoint(DevelopmentTask task) {
        stateStore.write(Path.of(task.checkpointPath()), task);
    }

    private Path checkpointPath(String taskId) {
        return Path.of("harness", "runtime", "tasks", taskId, "checkpoint.json");
    }
}
