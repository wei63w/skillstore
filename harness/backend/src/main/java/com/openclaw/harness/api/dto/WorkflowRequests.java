package com.openclaw.harness.api.dto;

import jakarta.validation.constraints.NotBlank;

public final class WorkflowRequests {

    private WorkflowRequests() {
    }

    public record StartWorkflowRequest(
            @NotBlank(message = "业务目标不能为空")
            String objective,
            @NotBlank(message = "工作区不能为空")
            String workspace,
            String featureDirectory
    ) {
    }
}
