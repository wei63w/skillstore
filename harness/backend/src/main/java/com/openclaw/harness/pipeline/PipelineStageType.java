package com.openclaw.harness.pipeline;

public enum PipelineStageType {
    GENERATE,
    VALIDATE_SCHEMA,
    REVIEW_DIFF,
    APPLY_PATCH,
    BACKEND_TEST,
    FRONTEND_TEST,
    E2E_TEST,
    SAST,
    DEPENDENCY_SCAN,
    DOCKER_BUILD,
    DEPLOY_DRY_RUN,
    GIT_SUBMIT,
    REPORT
}
