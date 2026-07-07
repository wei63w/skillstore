# Data Model: 完善 Harness CodeGenerationExecutor

## CodeGenerationRequest

**Purpose**: 表示一次代码生成请求。

**Fields**:

- `requestId`: 请求唯一标识。
- `taskId`: 所属 Harness 任务。
- `featureDirectory`: 目标 Spec Kit 功能目录。
- `workspaceRoot`: 工作区根目录。
- `allowedRoots`: 允许写入的目录。
- `mode`: `dry_run` 或 `apply`。
- `modelProvider`: 代码模型供应商标识。

## GenerationContext

**Purpose**: 表示从规格、计划、任务、契约和现有代码抽取的上下文包。

**Fields**:

- `specSummary`: 规格摘要。
- `planSummary`: 计划摘要。
- `taskItems`: 任务项摘要。
- `contractRefs`: 契约引用。
- `existingFiles`: 相关现有文件摘要。

## CodeModelRequest

**Purpose**: 发送给代码模型供应商的结构化请求。

**Fields**:

- `providerKey`: 供应商标识。
- `objective`: 生成目标。
- `context`: 生成上下文。
- `constraints`: 安全、路径和输出格式约束。

## PatchPlan

**Purpose**: 表示模型生成的可审查补丁计划。

**Fields**:

- `planId`: 补丁计划唯一标识。
- `taskId`: 所属任务。
- `summary`: 中文摘要。
- `fileChanges`: 文件变更项列表。
- `riskLevel`: 风险等级。
- `sourceRefs`: 规格、任务和契约来源引用。

## FileChange

**Purpose**: 表示单个文件创建或修改。

**Fields**:

- `path`: 目标路径。
- `changeType`: `create` 或 `modify`。
- `content`: 目标内容。
- `taskId`: 来源任务。
- `reason`: 变更原因。

## PatchApplyResult

**Purpose**: 表示 dry-run 或 apply 的执行结果。

**Fields**:

- `requestId`: 请求 ID。
- `mode`: 执行模式。
- `applied`: 是否已写入文件。
- `changedFiles`: 变更文件列表。
- `blockedReasons`: 阻塞原因列表。
- `reportPath`: 生成报告路径。

## GenerationRisk

**Purpose**: 表示生成阶段发现的安全或治理风险。

**Fields**:

- `riskType`: `path_escape`、`secret`、`payment`、`production_config`、`ambiguous_task`、`provider_unavailable`。
- `riskLevel`: `medium`、`high`、`critical`。
- `message`: 风险说明。
- `requiresHumanGate`: 是否需要人工确认。
