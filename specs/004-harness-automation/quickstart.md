# Quickstart: 完善 Agent Dev Harness 自主开发能力

本指南用于实现阶段验证 Harness 是否能自动执行 Spec Kit 开发闭环、持久化证据、执行测试、重试失败、更新 README、提交 GitHub 并生成报告。

## 前置条件

- 已阅读 [spec.md](./spec.md)、[plan.md](./plan.md)、[data-model.md](./data-model.md)。
- 本地具备 Java 21、Maven、Git 和 PowerShell。
- 当前仓库已配置 GitHub 远程仓库。
- 不需要真实云服务器、真实支付配置、真实密钥或真实部署权限。

## 设计产物检查

```powershell
Test-Path specs/004-harness-automation/plan.md
Test-Path specs/004-harness-automation/research.md
Test-Path specs/004-harness-automation/data-model.md
Test-Path specs/004-harness-automation/contracts/openapi.yaml
Test-Path specs/004-harness-automation/contracts/cli-contract.md
```

预期结果：所有命令返回 `True`。

## 后端测试验证

实现阶段完成后运行：

```powershell
Set-Location harness/backend
mvn test
mvn verify
```

预期结果：

- 单元测试和集成测试通过。
- 工作流编排、阶段恢复、命令执行、失败重试、人工门禁、报告生成和 Git 提交逻辑均有测试覆盖。
- 测试失败时 Harness 记录失败摘要和重试结果。

## Spec Kit 流水线验证

使用一个受控演示目标启动流水线：

```powershell
harness workflow start --objective "创建演示功能规格" --workspace "C:\Users\Administrator\Desktop\skillstore"
```

预期结果：

- 创建或定位唯一开发任务。
- 按 `specify → clarify → checklist → plan → tasks → implement` 顺序推进。
- 每个阶段在 `harness/runtime/tasks/`、`harness/runtime/logs/` 或 `harness/runtime/artifacts/` 下产生可追踪记录。

## 中断恢复验证

```powershell
harness workflow resume --task-id "<任务ID>"
```

预期结果：

- 30 秒内读取最近检查点。
- 从最近成功阶段继续。
- 不重复覆盖已完成产物。

## 失败重试验证

制造一个受控测试失败后运行：

```powershell
harness tests run --task-id "<任务ID>" --profile all
```

预期结果：

- 失败原因写入工具调用日志。
- 可自动修复的问题最多重试三轮。
- 三轮仍失败时创建人工确认请求，并将任务置为 `waiting_human`。

## 人工确认门禁验证

触发高风险动作示例：真实密钥、支付配置、端口开放或线上部署。

预期结果：

- Harness 不直接执行高风险动作。
- 生成结构化确认请求，包含待确认内容、风险等级、可选方案和后果。
- 确认记录进入最终报告。

## GitHub 交付验证

实现阶段完成一个小功能后运行：

```powershell
harness git submit --task-id "<任务ID>" --message "[harness] 新增：自主开发流水线执行器"
```

预期结果：

- README 开发记录已追加。
- 提交前通过密钥和高风险配置检查。
- 本地提交信息符合规范。
- 推送成功时记录提交哈希；推送失败时记录阻塞原因。

## 报告验证

```powershell
harness report generate --task-id "<任务ID>"
```

预期结果：

- 报告生成到 `harness/runtime/reports/`。
- 报告包含阶段状态、产物路径、代码变更摘要、测试结果、扫描结果、失败重试、人工确认、Git 状态和剩余风险。

## 成功判定

- Harness 能用同一套通用能力驱动受控演示需求完成 Spec Kit 流程。
- 中断恢复、失败重试、人工门禁和 GitHub 提交都有可审计记录。
- 不含真实密钥、真实支付配置或真实云端破坏性操作。
- OpenClaw Skill 商店仍保持为独立目标应用，不被写入 Harness 核心模块。

## 2026-07-07 验证结果

- 设计产物检查：`plan.md`、`research.md`、`data-model.md`、`contracts/openapi.yaml`、`contracts/cli-contract.md`、`tasks.md` 均存在。
- 后端测试验证：`mvn test` 通过 38 个测试。
- 构建验证：`mvn verify` 通过，并生成 Spring Boot jar 与 JaCoCo 报告。
- 依赖检查：`mvn dependency:tree -Dscope=runtime` 通过。
- 契约覆盖：REST 契约中的 workflow、run-tasks、tests、git-submit、report 端点均有实现或受控入口；CLI 契约已补充 git submit `--push false` 测试模式说明。
- 边界检查：Harness 核心未加入 OpenClaw Skill 商店业务流程规则，仅保留通用支付/密钥/生产配置风险扫描文案。
