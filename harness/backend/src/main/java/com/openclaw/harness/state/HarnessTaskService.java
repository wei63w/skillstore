package com.openclaw.harness.state;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class HarnessTaskService {

    private final InMemoryHarnessTaskRepository repository;
    private final AtomicLong sequence = new AtomicLong(1);

    public HarnessTaskService(InMemoryHarnessTaskRepository repository) {
        this.repository = repository;
    }

    public HarnessTask createTask(String title, String objective, String requestedBy) {
        String taskId = "task-%03d".formatted(sequence.getAndIncrement());
        Instant now = Instant.now();
        HarnessTask task = new HarnessTask(
                taskId,
                title,
                objective,
                requestedBy,
                "created",
                TaskStatus.CREATED,
                "harness/runtime/tasks/%s/checkpoint.json".formatted(taskId),
                now,
                now
        );
        return repository.save(task);
    }

    public Optional<HarnessTask> findTask(String taskId) {
        return repository.findById(taskId);
    }
}
