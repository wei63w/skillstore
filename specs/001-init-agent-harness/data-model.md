# Data Model: 初始化 Agent Dev Harness 工程骨架

## HarnessProject

**Purpose**: 表示 Agent Dev Harness 工程骨架本身的入口、边界和治理元数据。

**Fields**:

- `name`: 工程名称，固定为 Agent Dev Harness。
- `scope`: 当前阶段范围，第一阶段为后端调度核心骨架。
- `documentationLanguage`: 文档默认语言，固定为中文优先。
- `businessBoundary`: 与 OpenClaw Skill 商店业务应用的隔离说明。
- `status`: `draft`、`planned`、`implemented`、`validated`。

**Validation Rules**:

- 必须明确 Harness 不包含 Skill 商店业务规则。
- 必须引用 README、规格、计划和 quickstart。

## AgentModule

**Purpose**: 表示规划、编码、测试、构建部署、评审等 Agent 模块占位。

**Fields**:

- `moduleKey`: 模块唯一标识，例如 `planning`、`coding`、`testing`。
- `displayName`: 中文显示名称。
- `responsibility`: 模块职责说明。
- `inputArtifacts`: 输入产物列表。
- `outputArtifacts`: 输出产物列表。
- `status`: `placeholder`、`active`、`deprecated`。

**Relationships**:

- 一个 `HarnessProject` 包含多个 `AgentModule`。
- 一个 `AgentModule` 可产生多个 `TaskArtifact`。

**Validation Rules**:

- 每个核心 Agent 职责域必须至少有一个模块占位。
- 模块职责不得包含 Skill 商店业务规则。

## HarnessTask

**Purpose**: 表示长运行开发任务的调度和恢复单位。

**Fields**:

- `taskId`: 任务唯一标识。
- `title`: 任务标题。
- `currentPhase`: 当前阶段，例如需求、架构、编码、测试、构建、部署。
- `status`: `created`、`running`、`waitingHuman`、`failed`、`completed`、`rolledBack`。
- `checkpointRef`: 最近一次断点引用。
- `createdAt`: 创建时间。
- `updatedAt`: 更新时间。

**Relationships**:

- 一个 `HarnessTask` 包含多个 `ExecutionStep`。
- 一个 `HarnessTask` 可关联多个 `HumanGate`、`ContextSnapshot` 和 `TaskArtifact`。

**State Transitions**:

```text
created -> running -> waitingHuman -> running -> completed
created -> running -> failed -> running
running -> rolledBack
```

**Validation Rules**:

- 进入 `waitingHuman` 必须关联至少一个未解决 `HumanGate`。
- 进入 `completed` 必须存在最终报告产物。

## ExecutionStep

**Purpose**: 表示 Agent 或工具执行的单个可审计步骤。

**Fields**:

- `stepId`: 步骤唯一标识。
- `taskId`: 所属任务。
- `agentType`: 执行者类型。
- `action`: 执行动作。
- `inputRef`: 输入引用。
- `outputRef`: 输出引用。
- `toolCallRef`: 工具调用引用。
- `elapsedMs`: 耗时。
- `result`: `success`、`failed`、`skipped`。
- `createdAt`: 记录时间。

**Validation Rules**:

- 每个步骤必须能追踪输入、输出或跳过原因。
- 失败步骤必须记录错误摘要和后续动作。

## ContextSnapshot

**Purpose**: 表示长上下文压缩后的关键摘要。

**Fields**:

- `snapshotId`: 摘要唯一标识。
- `taskId`: 所属任务。
- `phase`: 生成阶段。
- `summary`: 中文摘要。
- `sourceRefs`: 原始输入引用。
- `createdAt`: 创建时间。

**Validation Rules**:

- 摘要必须能支持任务恢复和后续 Agent 接续。
- 摘要不得包含明文密钥或敏感凭据。

## HumanGate

**Purpose**: 表示需要人工确认的风险门禁。

**Fields**:

- `gateId`: 门禁唯一标识。
- `taskId`: 所属任务。
- `riskLevel`: `medium`、`high`、`critical`。
- `topic`: 待确认主题。
- `question`: 待确认问题。
- `options`: 可选方案。
- `decision`: 人工选择结果。
- `status`: `open`、`approved`、`rejected`、`cancelled`。
- `createdAt`: 创建时间。
- `resolvedAt`: 解决时间。

**Validation Rules**:

- 高风险基础设施、权限、凭据、支付、隐私和安全配置必须创建门禁。
- 未解决门禁必须阻止对应任务继续进入高风险动作。

## ToolPlugin

**Purpose**: 表示 Harness 可调用工具的注册信息。

**Fields**:

- `pluginKey`: 工具唯一标识。
- `displayName`: 中文名称。
- `capability`: 工具能力描述。
- `riskLevel`: 默认风险等级。
- `enabled`: 是否启用。
- `requiresHumanGate`: 是否需要人工确认。

**Validation Rules**:

- 高风险工具默认必须要求人工确认。
- 工具描述不得包含真实密钥或硬编码环境配置。

## TaskArtifact

**Purpose**: 表示需求、架构、源码、测试报告、构建记录、部署记录等阶段产物。

**Fields**:

- `artifactId`: 产物唯一标识。
- `taskId`: 所属任务。
- `type`: `requirement`、`architecture`、`source`、`testReport`、`buildLog`、
  `deployLog`、`runbook`、`other`。
- `path`: 产物路径。
- `checksum`: 校验值占位。
- `createdAt`: 创建时间。

**Validation Rules**:

- 产物路径必须位于约定产物目录内。
- 构建和部署产物必须能追踪到输入版本。

## DevelopmentRecord

**Purpose**: 表示 README 中每个小功能、开发修改或修复的记录。

**Fields**:

- `date`: 日期。
- `type`: 类型，例如规格、计划、实现、修复、文档。
- `scope`: 影响范围。
- `description`: 变更说明。
- `verification`: 验证结果。
- `githubStatus`: GitHub 提交状态。

**Validation Rules**:

- 每个可独立验证的小功能必须有记录。
- 如果 GitHub 提交失败，必须记录阻塞原因。
