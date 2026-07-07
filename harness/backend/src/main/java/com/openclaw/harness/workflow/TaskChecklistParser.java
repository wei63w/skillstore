package com.openclaw.harness.workflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class TaskChecklistParser {

    private static final Pattern TASK_PATTERN = Pattern.compile("^- \\[( |x|X)] (T\\d{3})\\s*(\\[P])?\\s*(\\[US\\d+])?\\s*(.+)$");

    public List<TaskChecklistItem> parse(Path tasksFile) {
        try {
            List<String> lines = Files.readAllLines(tasksFile);
            List<TaskChecklistItem> items = new ArrayList<>();
            for (int index = 0; index < lines.size(); index++) {
                Matcher matcher = TASK_PATTERN.matcher(lines.get(index));
                if (!matcher.matches()) {
                    continue;
                }
                String checked = matcher.group(1);
                String itemId = matcher.group(2);
                boolean parallel = matcher.group(3) != null;
                String story = matcher.group(4) == null ? null : matcher.group(4).replace("[", "").replace("]", "");
                String description = matcher.group(5).trim();
                items.add(new TaskChecklistItem(
                        itemId,
                        description,
                        parallel,
                        story,
                        List.of(),
                        checked.isBlank() ? TaskChecklistStatus.PENDING : TaskChecklistStatus.DONE,
                        index + 1
                ));
            }
            return items;
        } catch (IOException exception) {
            throw new IllegalStateException("读取任务清单失败: " + tasksFile, exception);
        }
    }
}
