# Data Model: 真实模型驱动 Harness 端到端流水线

## ModelProviderConfig

- `providerKey`: 供应商标识。
- `enabled`: 是否启用。
- `priority`: 选择优先级。
- `modelName`: 模型名称。
- `endpoint`: 供应商地址占位。
- `apiKeyEnv`: API Key 环境变量名，不保存真实值。
- `timeoutSeconds`: 请求超时时间。
- `maxRetries`: 供应商级重试次数。
- `responseFormat`: 期望模型输出格式。

**Validation**: `providerKey` 唯一；启用 provider 必须声明 `modelName` 和 `apiKeyEnv`；不得包含真实密钥。

## ModelOutputSchema

- `schemaId`: Schema 标识。
- `version`: Schema 版本。
- `requiredFields`: 必填字段。
- `allowedChangeTypes`: 允许的文件变更类型。
- `maxFileChanges`: 单次补丁最大文件数。

**Validation**: 所有模型输出必须包含 `planId`、`taskId`、`summary`、`fileChanges`、`riskLevel`、`sourceRefs`。

## GenerationAttempt

- `attemptId`: 尝试标识。
- `runId`: 所属流水线。
- `taskId`: 目标任务。
- `providerKey`: 使用供应商。
- `status`: `REQUESTED`、`SCHEMA_FAILED`、`REVIEW_PENDING`、`APPLIED`、`REJECTED`、`FAILED`。
- `promptSummary`: 脱敏 prompt 摘要。
- `responseSummary`: 脱敏响应摘要。
- `schemaErrors`: Schema 校验错误。
- `patchPlanPath`: 补丁计划证据路径。
- `createdAt`: 创建时间。

## PatchReview

- `reviewId`: 审阅标识。
- `attemptId`: 关联生成尝试。
- `changedFiles`: 变更文件列表。
- `diffSummary`: diff 摘要。
- `riskLevel`: 风险等级。
- `decision`: `PENDING`、`APPROVED`、`REJECTED`、`REQUIRES_HUMAN_GATE`。
- `reviewer`: `agent` 或人工标识。
- `reason`: 决策原因。

## PipelineRun

- `runId`: 流水线标识。
- `featureDirectory`: Spec Kit 功能目录。
- `targetWorkspace`: 目标应用工作区。
- `status`: `CREATED`、`RUNNING`、`WAITING_FOR_APPROVAL`、`SUCCEEDED`、`FAILED`、`BLOCKED`。
- `currentStage`: 当前阶段。
- `repairCount`: 修复轮次。
- `reportPath`: 最终报告路径。

## PipelineStageResult

- `stageId`: 阶段标识。
- `runId`: 所属流水线。
- `stageType`: `GENERATE`、`VALIDATE_SCHEMA`、`REVIEW_DIFF`、`APPLY_PATCH`、`BACKEND_TEST`、`FRONTEND_TEST`、`E2E_TEST`、`SAST`、`DEPENDENCY_SCAN`、`DOCKER_BUILD`、`DEPLOY_DRY_RUN`、`GIT_SUBMIT`、`REPORT`。
- `status`: `PASSED`、`FAILED`、`SKIPPED`、`WAITING_FOR_APPROVAL`。
- `startedAt`: 开始时间。
- `finishedAt`: 结束时间。
- `evidencePath`: 证据路径。
- `failureSummary`: 失败摘要。

## RepairLoop

- `repairId`: 修复循环标识。
- `runId`: 所属流水线。
- `attemptNumber`: 第几轮。
- `failureStage`: 失败阶段。
- `failureSummary`: 失败原因。
- `repairable`: 是否可自动修复。
- `nextAction`: `RETRY_GENERATION`、`RETRY_TEST`、`OPEN_HUMAN_GATE`、`STOP`。

## SandboxPolicyDecision

- `decisionId`: 决策标识。
- `runId`: 所属流水线。
- `operationType`: `FILE`、`COMMAND`、`NETWORK`、`CREDENTIAL`、`GIT`、`BUILD`、`DEPLOYMENT`。
- `target`: 操作目标。
- `riskLevel`: 风险等级。
- `decision`: `ALLOW`、`DENY`、`REQUIRE_APPROVAL`。
- `reason`: 决策原因。

## ApprovalRequest

- `approvalId`: 审批标识。
- `runId`: 所属流水线。
- `riskLevel`: 风险等级。
- `topic`: 审批主题。
- `question`: 待确认问题。
- `options`: 可选方案。
- `status`: `OPEN`、`APPROVED`、`REJECTED`。
- `decisionBy`: 决策人。
- `decisionAt`: 决策时间。
