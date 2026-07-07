package com.openclaw.harness.model;

import com.openclaw.harness.generation.FileChange;
import com.openclaw.harness.generation.PatchPlan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModelOutputSchemaValidator {

    public SchemaValidationResult validate(PatchPlan plan) {
        List<String> errors = new ArrayList<>();
        if (plan == null) {
            return SchemaValidationResult.fail(List.of("模型输出为空"));
        }
        requireText(plan.planId(), "planId", errors);
        requireText(plan.taskId(), "taskId", errors);
        requireText(plan.summary(), "summary", errors);
        if (plan.riskLevel() == null) {
            errors.add("riskLevel 不能为空");
        }
        if (plan.sourceRefs() == null || plan.sourceRefs().isEmpty()) {
            errors.add("sourceRefs 至少包含一个来源");
        }
        if (plan.fileChanges() == null || plan.fileChanges().isEmpty()) {
            errors.add("fileChanges 至少包含一个文件变更");
        } else {
            for (FileChange change : plan.fileChanges()) {
                validateChange(change, errors);
            }
        }
        return errors.isEmpty() ? SchemaValidationResult.pass() : SchemaValidationResult.fail(errors);
    }

    private void validateChange(FileChange change, List<String> errors) {
        if (change == null) {
            errors.add("fileChanges 不能包含空项");
            return;
        }
        requireText(change.path(), "fileChanges.path", errors);
        requireText(change.changeType(), "fileChanges.changeType", errors);
        requireText(change.taskId(), "fileChanges.taskId", errors);
        if (!List.of("add", "create", "modify", "delete").contains(change.changeType().toLowerCase())) {
            errors.add("不支持的 changeType: " + change.changeType());
        }
        if (!"delete".equalsIgnoreCase(change.changeType()) && (change.content() == null || change.content().isBlank())) {
            errors.add("非删除变更必须包含 content: " + change.path());
        }
    }

    private void requireText(String value, String field, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add(field + " 不能为空");
        }
    }
}
