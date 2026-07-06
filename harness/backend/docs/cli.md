# CLI 占位

后续 CLI 入口需满足 `specs/001-init-agent-harness/contracts/cli-contract.md`。

预期命令：

```bash
harness task create --title "<标题>" --objective "<目标>"
harness task status --task-id "<任务ID>"
harness task events --task-id "<任务ID>"
harness gate decide --gate-id "<门禁ID>" --decision approved --comment "<说明>"
harness report generate --task-id "<任务ID>"
```
