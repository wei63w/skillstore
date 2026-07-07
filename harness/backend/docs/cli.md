# CLI 占位

后续 CLI 入口需满足 `specs/001-init-agent-harness/contracts/cli-contract.md`。

预期命令：

```bash
harness task create --title "<标题>" --objective "<目标>"
harness task status --task-id "<任务ID>"
harness task events --task-id "<任务ID>"
harness gate decide --gate-id "<门禁ID>" --decision approved --comment "<说明>"
harness report generate --task-id "<任务ID>"
harness workflow start --objective "<业务目标>" --workspace "<仓库路径>" --feature-dir "<功能目录>"
harness workflow resume --task-id "<任务ID>"
harness workflow phases --task-id "<任务ID>"
```

## Workflow 命令

`harness workflow start` 用于启动 Spec Kit 严格流程编排，必须提供业务目标和工作区。默认按 `specify -> clarify -> checklist -> plan -> tasks -> implement` 验证阶段产物，任一阶段缺少必需产物时阻断并记录阶段错误。

`harness workflow resume` 用于从 `harness/runtime/tasks/<taskId>/checkpoint.json` 恢复最近阶段，输出 `taskId`、`resumedFrom`、`nextStage` 和 `restoreElapsedMs`。
