# Implementation Plan: 完善 Harness CodeGenerationExecutor

**Branch**: `005-code-generation-executor` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/005-code-generation-executor/spec.md`

## Summary

在现有 Harness 后端中把 `CodeGenerationExecutor` 从受控 Markdown 产物生成升级为“模型驱动的补丁计划与安全应用”能力：新增多供应商 `CodeModelProvider` 抽象、结构化模型请求/响应、补丁计划、文件变更、路径与敏感信息扫描、dry-run/apply 模式，并将生成结果接入测试、重试、报告和 Git 证据链。多模态模型仅预留后续 `VisualModelProvider` 扩展点，不作为本阶段依赖。

**Documentation Language**: 默认使用中文编写本功能产物。仅代码标识符、第三方协议、API 字段、英文专有名词或用户明确要求时使用英文。

## Technical Context

**Language/Version**: Java 21 LTS；Spring Boot 3.3.x；Maven；Harness 后端模块化单体。

**Primary Dependencies**: 沿用 Spring Web、Spring Validation、Jackson、JUnit 5；模型层第一版不引入供应商 SDK，采用统一接口和本地 stub provider，真实 OpenAI/Claude/Gemini/本地模型适配器后续可插拔接入。

**Storage**: 使用 `harness/runtime/artifacts/`、`harness/runtime/logs/`、`harness/runtime/reports/` 持久化模型请求摘要、补丁计划、文件变更、应用结果和风险扫描结果。

**Testing**: JUnit 5 单元测试、Spring Boot REST 合约测试、补丁 dry-run/apply 测试、路径越界与敏感内容扫描测试、模型 provider registry 测试。

**Target Platform**: 本地 Windows 开发环境、Linux 容器运行环境和后续演示服务器。

**Project Type**: Harness 后端服务 + CLI/REST 可调用代码生成执行能力。

**Performance Goals**: 单次受控补丁计划生成目标不超过 5 分钟；恢复目标不超过 30 秒；支持最多 3 条并发流水线复用模型 provider 抽象。

**Constraints**: 必须支持多家代码大模型供应商抽象；不得将密钥写入仓库；不得越界修改 Harness 核心或仓库外路径；真实支付、真实密钥、生产配置和多模态能力均不作为本阶段自动执行内容。

**Scale/Scope**: 第一版覆盖 `skill-store/backend`、`skill-store/frontend`、`skill-store/README.md`、`docs/`、`infra/` 的受控文件创建/修改；不实现真实外部模型 SDK 调用，不自动删除文件，不执行真实云部署。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Observability & Reproducibility**: PASS。模型请求、上下文摘要、补丁计划、文件变更、扫描结果和应用结果均进入运行产物与报告。
- **Human Risk Gates**: PASS。需求无法映射、越界路径、密钥、支付、生产配置、多轮失败均触发人工门禁。
- **Harness/Business Decoupling**: PASS。Harness 只生成补丁，不把商城业务规则硬编码进核心执行器。
- **Security Shift-Left**: PASS。路径校验、敏感扫描、dry-run、apply 前风险检查纳入核心流程。
- **Automation Loop**: PASS。生成结果接入 TestExecutor、RetryOrchestrator、DeliveryReport、GitSubmitExecutor。
- **Testing Gates**: PASS。计划包含 provider、补丁计划、路径扫描、dry-run/apply、REST 合约和报告测试。
- **Performance Baselines**: PASS。5 分钟生成与 30 秒恢复目标纳入 quickstart。
- **Deployment & Rollback**: PASS。本阶段不执行真实部署；补丁应用失败需记录回滚/失败状态。
- **Documentation**: PASS。生成中文 plan、research、data-model、contracts、quickstart。
- **README & GitHub Records**: PASS。每个小功能更新 README 并独立提交。
- **Spec Kit Workflow Gate**: PASS。`specify`、clarify 记录和 checklist 已完成，本步骤生成 plan。

**Gate Result**: PASS。无未解释宪章违规。

## Project Structure

### Documentation (this feature)

```text
specs/005-code-generation-executor/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── code-generation-contract.md
│   └── openapi.yaml
├── checklists/
└── tasks.md
```

### Source Code (repository root)

```text
harness/backend/src/main/java/com/openclaw/harness/
├── model/              # CodeModelProvider 抽象、provider registry、请求响应模型
├── generation/         # 补丁计划、文件变更、路径策略、应用器、扫描结果
├── executors/          # CodeGenerationExecutor 升级入口
├── api/                # CodeGenerationController
└── reports/            # 生成报告集成

harness/backend/src/test/java/com/openclaw/harness/
├── model/
├── generation/
├── executors/
└── api/
```

**Structure Decision**: 新增 `model/` 和 `generation/` 包承载通用模型与补丁领域；保留 `executors/CodeGenerationExecutor` 作为对外执行入口，避免供应商模型逻辑侵入业务应用或 Harness 调度核心。

## Spec Kit Workflow Evidence

- `specify`: 已完成，产物为 [spec.md](./spec.md)。
- `clarify`: 已完成，`spec.md` 已记录多供应商代码大模型必需、多模态后置的澄清。
- `checklist`: 已完成，[requirements.md](./checklists/requirements.md) 16/16 通过。
- `plan`: 当前产物为 [plan.md](./plan.md)。
- `tasks`: 计划完成后生成。
- `implement`: 在 `tasks.md` 生成后开始。

## Complexity Tracking

无宪章违规需要豁免。
