# Implementation Plan: 初始化 Agent Dev Harness 工程骨架

**Branch**: `001-init-agent-harness` | **Date**: 2026-07-06 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-init-agent-harness/spec.md`

## Summary

初始化 Agent Dev Harness 第一阶段工程骨架，重点交付 Java Spring Boot 后端调度核心
的模块边界、状态/日志/上下文/门禁/工具插件约定、REST 与 CLI 契约、中文文档入口和
质量门禁占位。Vue 3/React 控制台暂不作为本阶段必交付项，仅预留 `harness/console/`
边界，待后续调度核心稳定后再进入独立功能规划。

**Documentation Language**: All generated project documents default to Chinese.
Use English only for code identifiers, third-party protocol names, API fields, or
when the feature explicitly requires it.

## Technical Context

**Language/Version**: Java 21 LTS；Spring Boot 3.x；前端控制台后续采用 Vue 3 或 React，
本阶段不实现 UI。

**Primary Dependencies**: Spring Web、Spring Validation、Spring Actuator、
Spring Data JPA、Flyway、Jackson、JUnit 5、Mockito、Testcontainers 占位；
OpenAPI/Swagger 文档占位；Maven 作为构建入口。

**Storage**: 第一阶段以文件系统产物目录和内存/本地配置占位为主；状态模型按 MySQL 8
持久化设计，迁移脚本在实现阶段创建；Redis 暂不进入本阶段。

**Testing**: JUnit 5 单元测试、Spring Boot 集成测试、契约校验占位、安全/依赖扫描
占位；无前端 E2E，原因是本阶段不实现控制台 UI。

**Target Platform**: 本地开发环境、Linux 容器运行环境、后续演示服务器。

**Project Type**: 后端服务 + CLI-first 调度入口 + 后续可选 Web 控制台。

**Performance Goals**: 骨架阶段定义并保留验证入口：断点恢复加载目标不超过 30 秒；
支持最多 3 条独立开发流水线并发；单模块代码生成任务总耗时目标不超过 5 分钟。

**Constraints**: 禁止真实密钥、真实服务器地址、管理员凭据和线上破坏性操作；所有文档
中文优先；所有运行产物必须可追踪；README 必须记录本次计划变更并提交 GitHub。

**Scale/Scope**: 本阶段覆盖 Agent Dev Harness 骨架，不实现 OpenClaw Skill 商店业务
功能，不实现真实大模型调用、不执行真实云部署、不实现前端控制台。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Observability & Reproducibility**: 计划定义 `harness/runtime/` 下任务状态、结构化
  日志、上下文摘要、工具调用记录、阶段产物、报告和后续部署记录位置；所有产物将由
  Git 跟踪的配置和文档约定重建。
- **Human Risk Gates**: 计划定义人工确认门禁模型和合约，覆盖高风险基础设施、权限、
  凭据、支付、隐私、安全和三轮修复后仍失败的发布阻断问题。
- **Harness/Business Decoupling**: 计划只创建 `harness/` 边界；OpenClaw Skill 商店
  业务应用不进入本阶段源码结构。
- **Security Shift-Left**: 计划要求无真实密钥、无真实服务器地址、无破坏性操作，并
  预留 SAST、依赖扫描、输入校验和操作沙箱说明。
- **Automation Loop**: 计划覆盖需求、架构、数据模型、接口契约、实现任务、测试、
  扫描、构建、冒烟验证、报告和 README/GitHub 记录。
- **Testing Gates**: 本阶段要求后续任务生成单元测试、集成测试和契约校验；前端 E2E
  明确不适用，因为控制台不在本阶段实现；覆盖率门槛在代码实现阶段启用。
- **Performance Baselines**: Harness 的 30 秒恢复、3 条并发流水线、5 分钟单模块任务
  目标已纳入模型和 quickstart 验证占位。
- **Deployment & Rollback**: 本阶段只定义 Docker、冒烟检查和回滚占位，不触发真实云
  部署；后续实现必须补齐可执行脚本。
- **Documentation**: 已生成中文 plan、research、data-model、contracts、quickstart；
  README 将记录本次计划变更。
- **README & GitHub Records**: README 更新范围为新增计划阶段记录；本次变更提交信息
  使用 `[harness] 新增：工程骨架实施计划`。

**Gate Result**: PASS。无未解释宪章违规。

## Project Structure

### Documentation (this feature)

```text
specs/001-init-agent-harness/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── openapi.yaml
│   └── cli-contract.md
└── tasks.md              # 后续 /speckit-tasks 生成
```

### Source Code (repository root)

```text
harness/
├── backend/
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/openclaw/harness/
│   │   │   │   ├── AgentHarnessApplication.java
│   │   │   │   ├── agents/
│   │   │   │   ├── scheduler/
│   │   │   │   ├── state/
│   │   │   │   ├── context/
│   │   │   │   ├── tools/
│   │   │   │   ├── gates/
│   │   │   │   ├── observability/
│   │   │   │   ├── reports/
│   │   │   │   └── api/
│   │   │   └── resources/
│   │   └── test/java/com/openclaw/harness/
│   └── docs/
├── console/
│   └── README.md         # 后续 Vue 3/React 控制台占位，不在本阶段实现
├── runtime/
│   ├── tasks/
│   ├── logs/
│   ├── context/
│   ├── artifacts/
│   └── reports/
└── README.md

infra/
├── docker/
└── scripts/
```

**Structure Decision**: 采用 `harness/backend/` 作为 Java Spring Boot 调度核心；
`harness/console/` 仅保留前端控制台边界；`harness/runtime/` 作为本地运行产物约定；
`infra/` 保留容器与脚本位置。该结构满足 Harness 与 Skill 商店业务应用分离。

## Complexity Tracking

无宪章违规需要豁免。
