# Quickstart: 完善 Harness CodeGenerationExecutor

## 前置条件

- 已完成 `specify`、`clarify`、`checklist`、`plan`。
- 本地具备 Java 21、Maven、Git。
- 不需要真实模型密钥；第一版使用本地 stub provider。

## 运行验证

```powershell
Set-Location harness/backend
mvn test
mvn verify
mvn dependency:tree -Dscope=runtime
```

2026-07-07 验证结果：以上命令均通过，`mvn test` 与 `mvn verify` 均执行 49 个后端测试，0 failures，0 errors，0 skipped。

## REST Dry-run 示例

```powershell
$body = @{
  requestId = "demo-codegen"
  taskId = "task-demo"
  featureDirectory = "C:\\Users\\Administrator\\Desktop\\skillstore\\specs\\005-code-generation-executor"
  workspaceRoot = "C:\\Users\\Administrator\\Desktop\\skillstore"
  mode = "dry_run"
  modelProvider = "stub"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/harness/code-generation" `
  -ContentType "application/json" `
  -Body $body
```

## Dry-run 场景

调用代码生成接口，模式为 `dry_run`。

预期结果：

- 返回 `PatchApplyResult.applied = false`。
- 返回补丁计划和目标文件列表。
- 不写入 `skill-store/`。

## Apply 场景

调用代码生成接口，模式为 `apply`，目标路径位于允许目录。

预期结果：

- 写入允许目录中的文件。
- 记录变更文件、模型 provider、风险扫描和报告路径。

## 风险阻断场景

提交包含 `.env`、仓库外路径、路径穿越或真实密钥的文件变更。

预期结果：

- 不写入文件。
- 返回阻断原因。
- 生成风险记录，后续接入人工确认门禁。

## 供应商扩展

- 当前默认 `modelProvider` 为 `stub`，用于本地可复现验证。
- 后续接入 OpenAI、Claude、Gemini 或本地代码模型时，新增 `CodeModelProvider` 实现并注册为 Spring Bean。
- 多模态模型后置，不阻塞当前代码生成主链路。
