package com.openclaw.harness.reports;

import com.openclaw.harness.workflow.WorkflowStateStore;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ArtifactIndexService {

    private final AtomicLong sequence = new AtomicLong(1);
    private final ConcurrentHashMap<String, List<TaskArtifact>> artifacts = new ConcurrentHashMap<>();
    private final WorkflowStateStore stateStore;

    public ArtifactIndexService(WorkflowStateStore stateStore) {
        this.stateStore = stateStore;
    }

    public TaskArtifact add(String taskId, String type, String path) {
        TaskArtifact artifact = new TaskArtifact(
                "artifact-%03d".formatted(sequence.getAndIncrement()),
                taskId,
                type,
                path,
                null,
                Instant.now()
        );
        artifacts.computeIfAbsent(taskId, ignored -> new ArrayList<>()).add(artifact);
        stateStore.write(Path.of("harness", "runtime", "artifacts", taskId, artifact.artifactId() + ".json"), artifact);
        return artifact;
    }

    public List<TaskArtifact> findByTaskId(String taskId) {
        return List.copyOf(artifacts.getOrDefault(taskId, List.of()));
    }
}
