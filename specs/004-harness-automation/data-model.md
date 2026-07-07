# Data Model: 完善 Agent Dev Harness 自主开发能力

## DevelopmentTask

**Purpose**: 表示一次从业务目标到代码、测试、提交和报告的长运行自主开发任务。

**Fields**:

- `taskId`: 任务唯一标识。
- `objective`: 用户提交的业务开发目标。
- `featureDirectory`: 关联的 `specs/xxx-feature` 目录。
- `currentStage`: 当前 Spec Kit 或实现阶段。
- `status`: `created`、`running`、`waiting_human`、`failed`、`completed`、`cancelled`。
- `checkpointPath`: 最近成功阶段的检查点路径。
- `createdAt`: 创建时间。
- `updatedAt`: 更新时间。

**Relationships**:

- 一个 `DevelopmentTask` 包含多个 `PhaseExecution`、`ToolInvocation`、`RetryAttempt` 和 `DeliveryReport`。
- 一个 `DevelopmentTask` 可关联多个 `HumanConfirmationRequest`。

**Validation Rules**:

- `objective` 不得为空。
- 进入 `completed` 前必须存在最终报告和 README/Git 状态记录。
- 进入 `waiting_human` 必须存在未解决人工确认请求。

## SpecKitStage

**Purpose**: 表示 Spec Kit 严格流程中的阶段定义和顺序。

**Fields**:

- `stageKey`: `specify`、`clarify`、`checklist`、`plan`、`tasks`、`implement`。
- `orderIndex`: 阶段顺序。
- `requiredArtifacts`: 阶段必须产出的文件或目录。
- `canAutoRun`: 是否可由 Harness 自动执行。
- `riskGateRequired`: 是否在特定条件下需要人工确认。

**State Transitions**:

```text
specify -> clarify -> checklist -> plan -> tasks -> implement
```

**Validation Rules**:

- 阶段不得跳过或倒序执行。
- 任一阶段失败或缺少必需产物时，不得进入下一阶段。

## PhaseExecution

**Purpose**: 表示某个开发任务的一次阶段执行记录。

**Fields**:

- `phaseId`: 阶段执行唯一标识。
- `taskId`: 所属开发任务。
- `stageKey`: 阶段标识。
- `status`: `pending`、`running`、`passed`、`failed`、`blocked`、`skipped`。
- `inputSummary`: 输入摘要。
- `outputPaths`: 输出产物路径列表。
- `startedAt`: 开始时间。
- `finishedAt`: 结束时间。
- `elapsedMs`: 耗时。
- `errorSummary`: 错误摘要。

**Validation Rules**:

- 每次阶段开始和结束都必须持久化。
- 失败阶段必须记录错误摘要和后续动作。

## TaskChecklistItem

**Purpose**: 表示 `tasks.md` 中可执行、可验证和可更新状态的最小任务。

**Fields**:

- `itemId`: 任务项编号，例如 `T001`。
- `description`: 任务说明。
- `parallel`: 是否允许并行。
- `dependencies`: 依赖任务编号。
- `status`: `pending`、`in_progress`、`done`、`blocked`。
- `sourceLine`: 在 `tasks.md` 中的位置。

**Validation Rules**:

- 有依赖的任务必须在依赖完成后执行。
- 实现阶段必须以 `tasks.md` 状态为执行来源。

## ToolInvocation

**Purpose**: 表示 Harness 调用脚本、测试、构建、扫描、Git 或文件工具的一次记录。

**Fields**:

- `invocationId`: 调用唯一标识。
- `taskId`: 所属任务。
- `phaseId`: 所属阶段。
- `toolKey`: 工具标识。
- `workingDirectory`: 工作目录。
- `command`: 参数化命令摘要。
- `riskLevel`: `low`、`medium`、`high`、`critical`。
- `exitCode`: 退出码。
- `stdoutPath`: 标准输出路径。
- `stderrPath`: 错误输出路径。
- `elapsedMs`: 耗时。

**Validation Rules**:

- 高风险工具调用必须先存在已批准的人工确认。
- 输出日志不得包含明文密钥。

## RetryAttempt

**Purpose**: 表示失败后的自动修复尝试。

**Fields**:

- `retryId`: 重试唯一标识。
- `taskId`: 所属任务。
- `phaseId`: 所属阶段。
- `attemptNumber`: 第几轮，范围 1 到 3。
- `failureCategory`: `test`、`build`、`security`、`dependency`、`generation`、`unknown`。
- `failureSummary`: 失败摘要。
- `repairSummary`: 修复摘要。
- `verificationResult`: `passed`、`failed`、`blocked`。

**Validation Rules**:

- `attemptNumber` 不得超过 3。
- 第三轮仍失败必须创建人工确认请求。

## HumanConfirmationRequest

**Purpose**: 表示因歧义、高风险或三轮失败触发的人工确认。

**Fields**:

- `requestId`: 请求唯一标识。
- `taskId`: 所属任务。
- `trigger`: `ambiguity`、`high_risk_operation`、`security`、`payment`、`privacy`、`permission`、`retry_exhausted`。
- `riskLevel`: `medium`、`high`、`critical`。
- `question`: 待确认问题。
- `options`: 可选方案及后果。
- `status`: `open`、`approved`、`rejected`、`cancelled`。
- `decisionComment`: 人工回复。

**Validation Rules**:

- `open` 状态必须阻断关联高风险动作。
- 解决后必须写入审计日志和最终报告。

## GitDeliveryRecord

**Purpose**: 表示 README 更新、本地提交和 GitHub 推送结果。

**Fields**:

- `recordId`: 记录唯一标识。
- `taskId`: 所属任务。
- `readmeUpdated`: README 是否已更新。
- `commitMessage`: 提交信息。
- `commitHash`: 本地提交哈希。
- `pushStatus`: `not_started`、`pushed`、`failed`、`blocked`。
- `blockedReason`: 阻塞原因。
- `changedFiles`: 变更文件列表。

**Validation Rules**:

- 提交信息必须符合 `[模块] 动作：内容`。
- 推送失败必须记录原因并保留本地提交状态。

## DeliveryReport

**Purpose**: 表示一次开发流水线最终验收报告。

**Fields**:

- `reportId`: 报告唯一标识。
- `taskId`: 所属任务。
- `summary`: 中文摘要。
- `phaseResults`: 阶段结果列表。
- `artifactIndex`: 产物索引。
- `testResults`: 测试和扫描结果。
- `retrySummary`: 重试摘要。
- `humanGateSummary`: 人工确认摘要。
- `gitSummary`: Git 和 GitHub 结果。
- `residualRisks`: 剩余风险。
- `reportPath`: 报告路径。

**Validation Rules**:

- 流水线结束时必须生成报告。
- 报告必须引用所有关键产物路径。
