package com.openclaw.harness.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HarnessTaskServiceTest {

    @Test
    void createTaskInitializesCreatedStateAndCheckpointPath() {
        HarnessTaskService service = new HarnessTaskService(new InMemoryHarnessTaskRepository());

        HarnessTask task = service.createTask("示例任务", "验证骨架", "tester");

        assertThat(task.taskId()).startsWith("task-");
        assertThat(task.status()).isEqualTo(TaskStatus.CREATED);
        assertThat(task.currentPhase()).isEqualTo("created");
        assertThat(task.checkpointRef()).contains("harness/runtime/tasks/");
    }

    @Test
    void findTaskReturnsCreatedTask() {
        HarnessTaskService service = new HarnessTaskService(new InMemoryHarnessTaskRepository());
        HarnessTask created = service.createTask("示例任务", "验证骨架", "tester");

        assertThat(service.findTask(created.taskId())).contains(created);
    }
}
