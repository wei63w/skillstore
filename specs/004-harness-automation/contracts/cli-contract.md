# CLI Contract: 完善 Agent Dev Harness 自主开发能力

本合约定义 Harness 自主开发闭环的 CLI 草案，用于实现阶段的本地验证、CI 调用和后续控制台集成。命令应输出 JSON 或结构化文本，并将完整执行证据写入 `harness/runtime/`。

## 命令总览

```bash
harness workflow start --objective "<业务目标>" --workspace "<仓库路径>"
harness workflow resume --task-id "<任务ID>"
harness workflow status --task-id "<任务ID>"
harness workflow phases --task-id "<任务ID>"
harness tasks run --task-id "<任务ID>"
harness tests run --task-id "<任务ID>" --profile unit|integration|frontend|build|security|all
harness git submit --task-id "<任务ID>" --message "[harness] 新增：内容"
harness report generate --task-id "<任务ID>"
```

## `harness workflow start`

**用途**: 创建或定位开发任务，并启动 Spec Kit 严格流程。

**输入**:

- `--objective`: 必填，业务开发目标。
- `--workspace`: 必填，目标仓库路径。
- `--feature-dir`: 可选，已有功能规格目录。

**成功输出**:

```json
{
  "taskId": "task-004-harness-automation",
  "status": "running",
  "currentStage": "specify",
  "checkpointPath": "harness/runtime/tasks/task-004-harness-automation/checkpoint.json"
}
```

## `harness workflow resume`

**用途**: 从最近成功阶段恢复中断任务。

**输入**:

- `--task-id`: 必填，任务 ID。

**成功输出**:

```json
{
  "taskId": "task-004-harness-automation",
  "resumedFrom": "checklist",
  "nextStage": "plan",
  "restoreElapsedMs": 1200
}
```

## `harness workflow phases`

**用途**: 查询阶段执行记录。

**输入**:

- `--task-id`: 必填，任务 ID。

**成功输出**:

```json
[
  {
    "stageKey": "specify",
    "status": "passed",
    "elapsedMs": 2400,
    "outputPaths": ["specs/004-harness-automation/spec.md"]
  }
]
```

## `harness tasks run`

**用途**: 按 `tasks.md` 状态、依赖和并行标记执行实现任务。

**规则**:

- 只执行未完成任务。
- 依赖未完成时阻塞当前任务。
- 每个任务完成后更新任务状态和执行事件。

## `harness tests run`

**用途**: 执行指定测试或质量门禁，并记录输出。

**输入**:

- `--profile`: `unit`、`integration`、`frontend`、`build`、`security` 或 `all`。

**失败规则**:

- 可自动修复失败最多重试三轮。
- 第三轮仍失败时创建人工确认请求。

## `harness git submit`

**用途**: 更新 README 开发记录后创建本地提交并推送 GitHub。

**规则**:

- 提交前检查用户未提交变更、密钥和高风险配置。
- 提交信息必须符合 `[模块] 动作：内容`。
- 推送失败时记录阻塞原因和本地提交哈希。

## `harness report generate`

**用途**: 生成最终交付报告。

**成功输出**:

```json
{
  "taskId": "task-004-harness-automation",
  "reportPath": "harness/runtime/reports/task-004-harness-automation/report.md"
}
```
