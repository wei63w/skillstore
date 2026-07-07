package com.openclaw.harness.generation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatchApplier {

    private final PatchPathPolicy pathPolicy;
    private final GenerationRiskScanner riskScanner;

    public PatchApplier(PatchPathPolicy pathPolicy, GenerationRiskScanner riskScanner) {
        this.pathPolicy = pathPolicy;
        this.riskScanner = riskScanner;
    }

    public PatchApplyResult apply(CodeGenerationRequest request, PatchPlan plan) {
        List<String> blocked = new ArrayList<>(riskScanner.scan(plan));
        for (FileChange change : plan.fileChanges()) {
            if (!pathPolicy.allowed(request.workspaceRoot(), request.allowedRoots(), change.path())) {
                blocked.add("目标路径不允许: " + change.path());
            }
        }
        if (!blocked.isEmpty() || request.mode() == GenerationMode.DRY_RUN) {
            return new PatchApplyResult(request.requestId(), request.mode(), false,
                    plan.fileChanges().stream().map(FileChange::path).toList(), blocked, reportPath(request));
        }
        try {
            for (FileChange change : plan.fileChanges()) {
                Path target = request.workspaceRoot().resolve(change.path()).normalize();
                Files.createDirectories(target.getParent());
                if ("modify".equalsIgnoreCase(change.changeType()) && Files.exists(target)) {
                    Files.writeString(target, Files.readString(target) + change.content());
                } else {
                    Files.writeString(target, change.content());
                }
            }
            return new PatchApplyResult(request.requestId(), request.mode(), true,
                    plan.fileChanges().stream().map(FileChange::path).toList(), List.of(), reportPath(request));
        } catch (IOException exception) {
            return new PatchApplyResult(request.requestId(), request.mode(), false,
                    plan.fileChanges().stream().map(FileChange::path).toList(), List.of(exception.getMessage()), reportPath(request));
        }
    }

    private String reportPath(CodeGenerationRequest request) {
        return "harness/runtime/reports/%s-code-generation.json".formatted(request.requestId());
    }
}
