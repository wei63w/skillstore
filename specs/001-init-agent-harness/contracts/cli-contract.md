# CLI Contract: Agent Dev Harness 工程骨架

本文件定义第一阶段 CLI 入口草案，用于后续本地验证、CI 和自动化流程调用。该合约是
计划产物，不代表当前已经实现。

## 命令总览

```bash
harness task create --title "<标题>" --objective "<目标>"
harness task status --task-id "<任务ID>"
harness task events --task-id "<任务ID>"
harness gate decide --gate-id "<门禁ID>" --decision approved|rejected|cancelled --comment "<说明>"
harness report generate --task-id "<任务ID>"
```

## `harness task create`

**用途**: 创建长运行开发任务。

**输入**:

- `--title`: 必填，任务标题。
- `--objective`: 必填，任务目标。
- `--requested-by`: 可选，发起人或系统来源。

**成功输出**:

```json
{
  "taskId": "task-001",
  "status": "created",
  "currentPhase": "created"
}
```

## `harness task status`

**用途**: 查询任务状态和最近断点。

**输入**:

- `--task-id`: 必填，任务 ID。

**成功输出**:

```json
{
  "taskId": "task-001",
  "status": "running",
  "currentPhase": "planning",
  "checkpointRef": "harness/runtime/tasks/task-001/checkpoint.json"
}
```

## `harness task events`

**用途**: 查询任务结构化事件摘要。

**输入**:

- `--task-id`: 必填，任务 ID。

**成功输出**:

```json
[
  {
    "stepId": "step-001",
    "agentType": "planning",
    "action": "create-plan",
    "result": "success"
  }
]
```

## `harness gate decide`

**用途**: 提交人工确认门禁决策。

**输入**:

- `--gate-id`: 必填，门禁 ID。
- `--decision`: 必填，`approved`、`rejected` 或 `cancelled`。
- `--comment`: 可选，人工说明。

**规则**:

- 高风险动作在门禁解决前不得继续执行。
- 决策必须写入审计记录。

## `harness report generate`

**用途**: 为指定任务生成执行报告。

**输入**:

- `--task-id`: 必填，任务 ID。

**成功输出**:

```json
{
  "taskId": "task-001",
  "reportPath": "harness/runtime/reports/task-001-report.md"
}
```
