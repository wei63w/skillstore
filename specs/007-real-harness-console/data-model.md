# Data Model: 真实模型执行与 Harness Console

## RealModelProviderConfig

- `providerKey`: `deepseek`、`openai`、`claude`、`codex`、`stub`
- `displayName`: 展示名称
- `protocol`: `OPENAI_CHAT`、`OPENAI_RESPONSES`、`ANTHROPIC_MESSAGES`、`STUB`
- `endpoint`: API endpoint，不包含密钥
- `model`: 模型名
- `secretEnv`: 密钥环境变量名
- `enabled`: 是否启用
- `timeoutSeconds`: 单次请求超时
- `maxRetries`: 最大重试次数

## ModelInvocationEvidence

- `invocationId`: 模型调用 ID
- `runId`: 所属 pipeline run
- `providerKey`: provider
- `protocol`: 协议
- `startedAt` / `finishedAt`: 时间
- `latencyMs`: 延迟
- `attempt`: 当前尝试次数
- `status`: `SUCCEEDED`、`FAILED`、`BLOCKED`
- `httpStatus`: HTTP 状态码，未发起时为空
- `schemaValid`: 是否通过 PatchPlan Schema
- `errorCategory`: `MISSING_SECRET`、`TIMEOUT`、`RATE_LIMITED`、`INVALID_SCHEMA`、`PROVIDER_ERROR`
- `redactedMessage`: 脱敏错误
- `artifactPath`: 响应和校验证据路径

## PipelineRun

- `runId`: run ID
- `featureKey`: Spec Kit 功能目录名
- `targetProject`: `skill-store` 或其他目标
- `providerKey`: 模型 provider
- `dryRun`: 是否 dry-run
- `status`: `PENDING`、`RUNNING`、`WAITING_APPROVAL`、`BLOCKED`、`FAILED`、`SUCCEEDED`
- `currentStage`: 当前阶段
- `createdAt` / `updatedAt`: 时间
- `reportPath`: 总报告路径

## PipelineStageEvidence

- `stage`: `GENERATE_CODE`、`DIFF_REVIEW`、`APPLY_PATCH`、`BACKEND_TEST`、`FRONTEND_TEST`、`E2E_TEST`、`SAST_SCAN`、`DEPENDENCY_SCAN`、`DOCKER_BUILD`、`GIT_SUBMIT`、`REPORT`
- `status`: `PENDING`、`RUNNING`、`SUCCEEDED`、`FAILED`、`BLOCKED`、`SKIPPED`
- `startedAt` / `finishedAt`
- `command`: 脱敏后的命令
- `exitCode`: 退出码
- `summary`: 中文摘要
- `logPath`: 日志路径
- `artifactPaths`: 证据文件
- `riskLevel`: `LOW`、`MEDIUM`、`HIGH`、`CRITICAL`

## ApprovalRequest

- `approvalId`: 审批 ID
- `runId`: 所属 run
- `stage`: 审批阶段
- `riskLevel`: 风险等级
- `reason`: 审批原因
- `options`: 可选操作
- `status`: `PENDING`、`APPROVED`、`REJECTED`
- `decisionBy`: 决策人
- `decisionReason`: 决策理由
- `decidedAt`: 决策时间

## ConsoleRunView

- `runs`: 左侧列表
- `selectedRun`: 当前 run
- `stages`: 中间时间线
- `logs`: 右侧日志
- `diff`: 右侧 diff 摘要
- `approvals`: 右侧审批请求
