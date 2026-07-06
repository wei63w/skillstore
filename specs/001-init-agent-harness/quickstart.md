# Quickstart: 初始化 Agent Dev Harness 工程骨架

本指南用于后续实现阶段验证 Agent Dev Harness 工程骨架是否满足规格和计划要求。当前
阶段是计划产物，命令会在实现阶段补齐。

## 前置条件

- 已阅读 [spec.md](./spec.md) 和 [plan.md](./plan.md)。
- 本地具备 Java 21 LTS 和 Maven。
- 当前仓库已配置 GitHub 远程仓库。
- 不需要真实云服务器、真实密钥或真实支付配置。

## 预期目录检查

实现完成后，应能看到以下结构：

```text
harness/
├── backend/
├── console/
├── runtime/
└── README.md

infra/
├── docker/
└── scripts/
```

验证方式：

```bash
dir harness
dir harness\backend
dir harness\runtime
```

## 本地启动验证

实现阶段补齐后，预期命令：

```bash
cd harness\backend
mvn spring-boot:run
```

预期结果：

- 服务本地启动成功。
- 健康检查接口返回可用状态。
- 不需要真实外部凭据。

## 合约验证

REST 合约参考：[contracts/openapi.yaml](./contracts/openapi.yaml)

CLI 合约参考：[contracts/cli-contract.md](./contracts/cli-contract.md)

实现阶段应验证：

```bash
curl http://localhost:8080/api/harness/health
harness task create --title "示例任务" --objective "验证骨架"
harness task status --task-id "<任务ID>"
```

## 可观测产物验证

执行一个示例任务后，应生成或更新以下产物类别：

- 任务状态：`harness/runtime/tasks/`
- 结构化日志：`harness/runtime/logs/`
- 上下文摘要：`harness/runtime/context/`
- 阶段产物：`harness/runtime/artifacts/`
- 执行报告：`harness/runtime/reports/`

## 人工确认门禁验证

实现阶段应能模拟一个高风险动作，并确认系统不会直接执行，而是生成门禁记录。

预期检查点：

- 门禁包含待确认内容、风险等级、可选方案和后果说明。
- 未确认前任务状态为 `waitingHuman`。
- 提交确认或拒绝后，审计记录可追踪。

## 测试与质量门禁

实现阶段应补齐并执行：

```bash
cd harness\backend
mvn test
mvn verify
```

预期结果：

- 单元测试通过。
- 集成测试通过。
- 覆盖率报告生成。
- SAST 和依赖扫描占位或实际命令有明确结果。

## README 与 GitHub 记录验证

每完成一个小功能，必须：

1. 更新根目录 README 的开发记录。
2. 记录日期、类型、范围、说明、验证结果和 GitHub 状态。
3. 使用项目提交规范提交并推送。

示例提交信息：

```text
[harness] 新增：工程骨架后端模块
```

## 成功判定

- 新开发者可在 10 分钟内理解 Harness 入口、边界和扩展点。
- 所有核心责任域都有目录或占位说明。
- 所有运行产物类别都有位置和用途说明。
- 无真实密钥、真实服务器地址或破坏性线上操作。
- README 和 GitHub 提交记录完整。
