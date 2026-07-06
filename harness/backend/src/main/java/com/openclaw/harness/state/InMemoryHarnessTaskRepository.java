package com.openclaw.harness.state;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryHarnessTaskRepository {

    private final ConcurrentHashMap<String, HarnessTask> tasks = new ConcurrentHashMap<>();

    public HarnessTask save(HarnessTask task) {
        tasks.put(task.taskId(), task);
        return task;
    }

    public Optional<HarnessTask> findById(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }
}
