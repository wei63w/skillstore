package com.openclaw.harness.workflow;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskExecutionPlanner {

    public List<TaskChecklistItem> pendingInExecutionOrder(List<TaskChecklistItem> items) {
        return items.stream()
                .filter(item -> item.status() != TaskChecklistStatus.DONE)
                .sorted(Comparator.comparing(TaskChecklistItem::itemId))
                .toList();
    }

    public boolean ready(TaskChecklistItem item, List<TaskChecklistItem> allItems) {
        return item.dependencies().stream()
                .allMatch(dependency -> allItems.stream()
                        .anyMatch(candidate -> candidate.itemId().equals(dependency)
                                && candidate.status() == TaskChecklistStatus.DONE));
    }
}
