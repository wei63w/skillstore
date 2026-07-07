# Implementation Plan: 完善 Agent Dev Harness 自主开发能力

**Branch**: `004-harness-automation` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/004-harness-automation/spec.md`

## Summary

在既有 Agent Dev Harness Java Spring Boot 后端骨架上补齐自主开发闭环能力：新增 Spec Kit 流程编排器、任务执行器、代码生成执行器、测试执行器、失败重试、Git 提交、报告生成和持久化运行证据。实现阶段以本地仓库、受控模板、命令执行和文件系统持久化为第一版边界；OpenClaw Skill 商店仅作为后续目标业务案例，不把商城业务规则写入 Harness 核心。

**Documentation Language**: 默认使用中文编写本功能产物。仅代码标识符、第三方协议、API 字段、英文专有名词或用户明确要求时使用英文。

## Technical Context

**Language/Version**: Java 21 LTS；Spring Boot 3.3.x；Maven；PowerShell 脚本作为 Windows 本地开发入口；后续可扩展 Linux shell 入口。

**Primary Dependencies**: 沿用现有 Harness 后端依赖：Spring Web、Spring Validation、Spring Actuator、Jackson、JUnit 5、Mockito；新增实现优先使用 JDK `ProcessBuilder`、NIO 文件 API 和现有服务，不引入工作流引擎或消息队列。

**Storage**: 第一版使用 `harness/runtime/` 文件系统持久化任务状态、阶段记录、工具调用日志、上下文摘要、重试记录、报告和 Git 结果；保留后续迁移到 MySQL 8 的模型边界。

**Testing**: JUnit 5 单元测试、Spring Boot 集成测试、CLI/REST 契约测试、文件持久化恢复测试、失败重试测试、Git 提交 dry-run 或受控临时仓库测试；安全与依赖扫描通过脚本占位和命令结果记录进入报告。

**Target Platform**: 本地 Windows 开发环境、Linux 容器运行环境和后续演示服务器；当前计划不执行真实云端破坏性部署。

**Project Type**: 后端调度服务 + CLI-first 自动化执行器 + 文件系统运行时产物；前端控制台保持后续可选扩展。

**Performance Goals**: 任务断点恢复加载目标 ≤30 秒；单模块代码生成任务目标 ≤5 分钟；最多 3 条独立开发流水线并发；测试执行反馈必须记录开始、结束、耗时和失败原因。

**Constraints**: 必须严格执行 `specify → clarify → checklist → plan → tasks → implement`；三轮自动修复后必须进入人工确认；不得提交明文密钥、真实支付配置、真实服务器地址或破坏性部署配置；不得覆盖用户未提交变更；每个小功能必须更新 README 并提交 GitHub。

**Scale/Scope**: 覆盖单仓库内 1-3 条并发开发流水线、单个功能目录的 Spec Kit 全流程、受控本地命令执行、报告和 Git 证据；不实现真实多模型 Agent 集群、不接入真实云 API、不实现商城业务闭环。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Observability & Reproducibility**: PASS。计划通过 `PipelineRun`、`PhaseExecution`、`ToolInvocation`、`RetryAttempt`、`DeliveryReport` 和 `harness/runtime/` 产物索引记录阶段输入、输出、耗时、错误、文件路径和 Git 状态，支持回放和断点恢复。
- **Human Risk Gates**: PASS。计划在需求歧义、高风险配置、凭据/支付/隐私/权限/部署、三轮失败后生成 `HumanConfirmationRequest`，并阻断后续动作。
- **Harness/Business Decoupling**: PASS。实现只在 `harness/backend` 增强通用编排能力；OpenClaw Skill 商店作为目标规格路径或外部工作区输入，不进入核心调度规则。
- **Security Shift-Left**: PASS。提交前执行密钥/高风险配置检查，测试执行器记录 SAST 和依赖扫描命令，命令执行器限制工作目录和风险动作门禁。
- **Automation Loop**: PASS。计划覆盖规格、澄清、清单、计划、任务、实现、测试、扫描、README、GitHub 和最终报告。
- **Testing Gates**: PASS。任务阶段必须生成单元、集成、恢复、重试、命令执行和报告测试；覆盖率不足、高危漏洞或关键流程失败阻断完成。
- **Performance Baselines**: PASS。恢复、并发、单模块生成耗时目标已进入技术上下文和 quickstart 验证。
- **Deployment & Rollback**: PASS。当前功能不执行真实部署；仅记录构建/部署命令结果和风险门禁。真实部署后续单独规格处理。
- **Documentation**: PASS。计划生成 research、data-model、contracts、quickstart，后续 tasks 和实现报告继续中文优先。
- **README & GitHub Records**: PASS。计划阶段会更新根 README 开发记录，并以 `[harness] 新增：自主开发能力实施计划` 提交推送。
- **Spec Kit Workflow Gate**: PASS。`specify`、`clarify`、`checklist` 已完成；本步骤生成 `plan`；后续必须执行 `tasks` 和 `implement`。

**Gate Result**: PASS。无未解释宪章违规。

## Project Structure

### Documentation (this feature)

```text
specs/004-harness-automation/
├── plan.md
├── spec.md
├── checklists/
│   ├── requirements.md
│   └── harness-workflow.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── cli-contract.md
│   └── openapi.yaml
└── tasks.md              # 后续 /speckit-tasks 生成
```

### Source Code (repository root)

```text
harness/
├── backend/
│   ├── pom.xml
│   ├── src/main/java/com/openclaw/harness/
│   │   ├── workflow/        # Spec Kit 阶段编排、状态推进、恢复
│   │   ├── executors/       # 代码生成、测试、命令、README、Git 执行器
│   │   ├── retry/           # 失败分类、最多三轮自动修复策略
│   │   ├── state/           # 复用并扩展开发任务状态
│   │   ├── observability/   # 复用并扩展结构化事件和工具调用记录
│   │   ├── gates/           # 复用人工确认门禁
│   │   ├── reports/         # 执行报告和产物索引
│   │   ├── tools/           # 工具插件注册与命令能力描述
│   │   └── api/             # REST API 和 DTO
│   └── src/test/java/com/openclaw/harness/
├── runtime/
│   ├── tasks/
│   ├── logs/
│   ├── context/
│   ├── artifacts/
│   └── reports/
└── README.md

infra/
└── scripts/
    ├── check-harness-docs.ps1
    └── harness-smoke.ps1
```

**Structure Decision**: 在现有 `harness/backend` 模块化单体内新增通用编排包，保留 `harness/runtime` 文件系统持久化边界；不新增独立服务、不引入前端控制台实现、不修改 `skill-store/` 业务代码。

## Spec Kit Workflow Evidence

- `specify`: 已完成，产物为 [spec.md](./spec.md)。
- `clarify`: 已完成，`spec.md` 的 `Clarifications` 记录“无关键阻塞歧义”。
- `checklist`: 已完成，[requirements.md](./checklists/requirements.md) 和 [harness-workflow.md](./checklists/harness-workflow.md) 均通过。
- `plan`: 当前产物为 [plan.md](./plan.md)。
- `tasks`: 计划完成后由 `$speckit-tasks` 生成 [tasks.md](./tasks.md)。
- `implement`: 在 `tasks.md` 生成并通过后开始。

## Complexity Tracking

无宪章违规需要豁免。
