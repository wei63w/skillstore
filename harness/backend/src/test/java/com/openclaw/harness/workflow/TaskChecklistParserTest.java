package com.openclaw.harness.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TaskChecklistParserTest {

    private final TaskChecklistParser parser = new TaskChecklistParser();
    private final TaskExecutionPlanner planner = new TaskExecutionPlanner();

    @Test
    void parsesCheckboxesParallelMarkersAndStories() throws Exception {
        Path tasks = Files.createTempFile("tasks", ".md");
        Files.writeString(tasks, """
                - [x] T001 Done item in `README.md`
                - [ ] T002 [P] [US1] Pending story item in `src/Test.java`
                """);

        var items = parser.parse(tasks);

        assertThat(items).hasSize(2);
        assertThat(items.get(0).status()).isEqualTo(TaskChecklistStatus.DONE);
        assertThat(items.get(1).parallel()).isTrue();
        assertThat(items.get(1).story()).isEqualTo("US1");
        assertThat(planner.pendingInExecutionOrder(items)).extracting(TaskChecklistItem::itemId).containsExactly("T002");
    }
}
