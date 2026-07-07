package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.sandbox.SandboxDecision;
import com.openclaw.harness.sandbox.SandboxOperation;
import com.openclaw.harness.sandbox.SandboxOperationType;
import com.openclaw.harness.sandbox.SandboxPolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SandboxController {

    private final SandboxPolicyService sandboxPolicyService;

    public SandboxController(SandboxPolicyService sandboxPolicyService) {
        this.sandboxPolicyService = sandboxPolicyService;
    }

    @PostMapping("/api/harness/sandbox/evaluate")
    public ApiResponse<SandboxDecision> evaluate(@Valid @RequestBody SandboxEvaluationRequest request) {
        return ApiResponse.ok(sandboxPolicyService.evaluate(new SandboxOperation(
                request.runId(),
                SandboxOperationType.valueOf(request.operationType()),
                request.target()
        )));
    }

    public record SandboxEvaluationRequest(
            String runId,
            @NotBlank String operationType,
            @NotBlank String target
    ) {
    }
}
