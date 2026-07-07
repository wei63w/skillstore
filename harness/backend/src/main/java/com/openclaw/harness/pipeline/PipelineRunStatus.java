package com.openclaw.harness.pipeline;

public enum PipelineRunStatus {
    CREATED,
    RUNNING,
    WAITING_FOR_APPROVAL,
    SUCCEEDED,
    FAILED,
    BLOCKED
}
