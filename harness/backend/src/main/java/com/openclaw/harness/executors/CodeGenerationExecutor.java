package com.openclaw.harness.executors;

import com.openclaw.harness.generation.CodeGenerationRequest;
import com.openclaw.harness.generation.GenerationContext;
import com.openclaw.harness.generation.GenerationContextLoader;
import com.openclaw.harness.generation.PatchApplyResult;
import com.openclaw.harness.generation.PatchApplier;
import com.openclaw.harness.model.CodeModelProviderRegistry;
import com.openclaw.harness.model.CodeModelRequest;
import com.openclaw.harness.model.StubCodeModelProvider;
import com.openclaw.harness.workflow.TaskChecklistItem;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerationExecutor {

    private final CodeModelProviderRegistry providerRegistry;
    private final GenerationContextLoader contextLoader;
    private final PatchApplier patchApplier;
    private final WorkflowStateStore stateStore;

    public CodeGenerationExecutor() {
        this(new CodeModelProviderRegistry(List.of(new StubCodeModelProvider())),
                new GenerationContextLoader(),
                new PatchApplier(new com.openclaw.harness.generation.PatchPathPolicy(), new com.openclaw.harness.generation.GenerationRiskScanner()),
                new WorkflowStateStore());
    }

    public CodeGenerationExecutor(CodeModelProviderRegistry providerRegistry, GenerationContextLoader contextLoader, PatchApplier patchApplier, WorkflowStateStore stateStore) {
        this.providerRegistry = providerRegistry;
        this.contextLoader = contextLoader;
        this.patchApplier = patchApplier;
        this.stateStore = stateStore;
    }

    public PatchApplyResult generate(CodeGenerationRequest request) {
        GenerationContext context = contextLoader.load(request.featureDirectory());
        var response = providerRegistry.get(request.modelProvider()).generatePatch(new CodeModelRequest(
                request.modelProvider(),
                request.taskId(),
                context,
                List.of("输出结构化 PatchPlan", "禁止明文密钥", "禁止越界路径")
        ));
        if (!response.successful()) {
            return new PatchApplyResult(request.requestId(), request.mode(), false, List.of(), response.blockedReasons(), null);
        }
        PatchApplyResult result = patchApplier.apply(request, response.patchPlan());
        stateStore.write(Path.of(result.reportPath()), result);
        return result;
    }

    public Path generateMarkdownArtifact(Path workspaceRoot, TaskChecklistItem item, String content) {
        Path output = workspaceRoot.resolve("harness/runtime/artifacts/%s.md".formatted(item.itemId())).normalize();
        if (!output.startsWith(workspaceRoot.normalize())) {
            throw new IllegalArgumentException("生成产物必须位于工作区内");
        }
        try {
            Files.createDirectories(output.getParent());
            Files.writeString(output, "# " + item.itemId() + "\n\n" + content + "\n");
            return output;
        } catch (IOException exception) {
            throw new IllegalStateException("生成受控产物失败: " + output, exception);
        }
    }
}
