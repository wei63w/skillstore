package com.openclaw.harness.model;

import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.generation.FileChange;
import com.openclaw.harness.generation.PatchPlan;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HttpCodeModelProvider implements CodeModelProvider {

    @Override
    public String providerKey() {
        return "fake";
    }

    @Override
    public CodeModelResponse generatePatch(CodeModelRequest request) {
        PatchPlan plan = new PatchPlan(
                "http-fake-" + Math.abs(request.objective().hashCode()),
                request.objective(),
                "fake HTTP provider 生成的可校验补丁计划",
                List.of(new FileChange(
                        "skill-store/README.md",
                        "modify",
                        "\n## Harness 真实模型流水线记录\n\n- " + request.objective() + "\n",
                        request.objective(),
                        "记录真实模型 provider dry-run 结果"
                )),
                RiskLevel.LOW,
                List.of("spec.md", "plan.md", "tasks.md")
        );
        return new CodeModelResponse(true, plan, List.of());
    }
}
