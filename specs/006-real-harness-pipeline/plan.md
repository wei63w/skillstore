# Implementation Plan: 真实模型驱动 Harness 端到端流水线

**Branch**: `006-real-harness-pipeline` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/006-real-harness-pipeline/spec.md`

## Summary

本功能把 Harness 从“stub 代码生成演示”推进为“真实代码模型驱动的端到端自主交付流水线”。技术路线是在现有 `CodeModelProvider`、`CodeGenerationExecutor`、`TestExecutor`、`GitSubmitExecutor`、`HumanGateService` 基础上扩展，而不是重写 Harness：新增模型供应商配置占位、真实 HTTP 代码模型 provider、模型输出 Schema 校验、补丁 diff 审阅、自动修复循环、Skill Store 目标应用边界、E2E/SAST/依赖扫描、Docker 构建、部署 dry-run/审批、权限沙箱和流水线报告。

第一实现切片优先交付可验证 MVP：真实 provider 配置与本地可替代 provider、模型输出校验、diff review、端到端 pipeline dry-run，确保不需要真实密钥也能完成本地验证；真实云部署和高危操作只生成审批请求，不自动执行。

**Documentation Language**: All generated project documents default to Chinese.
Use English only for code identifiers, third-party protocol names, API fields, or
when the feature explicitly requires it.

## Technical Context

**Language/Version**: Java 21；Harness 后端为 Spring Boot 3.3.7；目标应用 Skill Store 为 Java Spring Boot + Vue 3/TypeScript。

**Primary Dependencies**: Spring Web、Spring Validation、Jackson、JUnit 5、现有 Maven/Jacoco；真实模型第一阶段使用 JDK HTTP Client 或 Spring 原生能力，避免直接绑定供应商 SDK；E2E/前端验证调用目标应用既有 npm 脚本；Docker/SAST/依赖扫描通过命令执行器插件化调用。

**Storage**: 本阶段继续使用文件系统持久化 `harness/runtime` 下的任务状态、模型尝试、diff review、扫描报告、构建报告、部署报告和最终流水线报告；后续可迁移到数据库。

**Testing**: Maven + JUnit 5；控制器使用 Spring MockMvc；命令执行、模型 provider、Schema 校验、sandbox、pipeline 编排使用单元测试；Skill Store 前端 E2E/SAST/Docker 在 Harness 内以命令计划和 dry-run 结果验证。

**Target Platform**: 本地 Windows 开发环境与演示服务器兼容；命令执行必须使用路径白名单与审批门禁；真实远程部署默认 dry-run。

**Project Type**: 后端调度服务 + 目标应用生成执行器 + 命令插件池。

**Performance Goals**: 单模块代码生成目标 5 分钟内完成；断点恢复 30 秒内；最多 3 条流水线并发跟踪；本地 dry-run pipeline 在 60 秒内完成验证。

**Constraints**: 不提交真实密钥；不自动执行高危部署；不把 Skill Store 业务规则写入 Harness 核心；所有模型输出先校验再审阅再应用；三轮自动修复后必须进入人工门禁。

**Scale/Scope**: 支持至少 2 个真实模型配置占位和 1 个本地 stub/fake provider；支持一个 Skill Store 目标应用；支持 backend/frontend/e2e/security/dependency/docker/deploy 七类 pipeline stage。

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Observability & Reproducibility**: 计划新增 `GenerationAttempt`、`PatchReview`、`PipelineRun`、`PipelineStageResult`、`SandboxDecision`、`ApprovalRequest`、各类报告文件，全部写入 `harness/runtime` 并可由 request/run id 回放。
- **Human Risk Gates**: 模型配置缺失、密钥访问、高危路径、权限/部署/支付/隐私、安全高危、三轮修复失败均进入 `HumanGateService` 或新审批记录。
- **Harness/Business Decoupling**: Harness 只维护 provider、校验、审阅、流水线、命令、安全和部署抽象；Skill Store 仅作为 target workspace 和 allowed roots，不把商城业务规则写入核心模块。
- **Security Shift-Left**: 模型密钥使用环境变量引用；模型输出 Schema 校验；补丁路径、内容、diff 风险扫描；SAST/依赖扫描在 build 前执行；部署默认 dry-run。
- **Automation Loop**: 计划覆盖生成、校验、审阅、应用、测试、扫描、构建、部署、Git 提交、README、报告。
- **Testing Gates**: 每个 provider/config/schema/review/pipeline/sandbox stage 均设测试任务；失败重试三轮；高危问题阻断。
- **Performance Baselines**: 使用 spec 指定基线；外部模型延迟不计入本地 stub 验证；恢复状态通过文件定位目标 30 秒内。
- **Deployment & Rollback**: Docker build executor、deployment dry-run executor、rollback plan record；真实远程部署需审批。
- **Documentation**: 输出 research、data-model、OpenAPI 契约、provider 配置指南、quickstart、测试/安全/构建/部署报告。
- **README & GitHub Records**: 每个独立切片更新 README 并提交：真实模型配置、Schema/diff 审阅、端到端流水线、安全构建部署、权限沙箱审批。
- **Spec Kit Workflow Gate**: `specify` 已完成；规格质量清单已通过。`clarify` 未单独执行，当前 spec 无开放澄清，计划在本功能记录为“无阻塞歧义，需在后续正式 clarify 中回填”；`checklist` 已有 requirements 清单，但仍建议后续补一次专项 checklist。当前计划继续生成设计产物，实施前以 tasks 和 checklist 状态作为门禁。

## Project Structure

### Documentation (this feature)

```text
specs/006-real-harness-pipeline/
├── plan.md
├── spec.md
├── checklists/
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── real-harness-pipeline-contract.md
│   └── openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
harness/backend/
├── src/main/java/com/openclaw/harness/
│   ├── model/              # CodeModelProvider 扩展、真实 provider、配置、Schema 校验
│   ├── generation/         # PatchPlan、diff review、attempt 记录、repair loop 支撑
│   ├── pipeline/           # 端到端 pipeline run/stage 编排
│   ├── sandbox/            # 操作权限策略、风险决策、审批请求
│   ├── deployment/         # Docker build、部署 dry-run、rollback record
│   ├── security/           # SAST/依赖扫描命令计划与结果
│   ├── executors/          # 复用 Command/Test/Git 执行器
│   └── api/                # pipeline/model/review/sandbox REST 入口
├── src/main/resources/
│   ├── application.yml
│   └── application-models.example.yml
├── src/test/java/com/openclaw/harness/
│   ├── model/
│   ├── generation/
│   ├── pipeline/
│   ├── sandbox/
│   ├── deployment/
│   ├── security/
│   └── api/
└── docs/
    ├── model-provider.md
    ├── pipeline-operator-guide.md
    ├── sandbox-policy.md
    ├── deployment-runbook.md
    ├── test-coverage.md
    └── security-scan.md

skill-store/
├── backend/
├── frontend/
└── README.md
```

**Structure Decision**: 在现有 Harness 模块化单体中扩展新包；target Skill Store 保持独立工程。模型供应商、Schema 校验、diff 审阅、pipeline、sandbox、安全扫描、部署执行均属于 Harness 通用能力。

## Spec Kit Workflow Evidence

- `specify`: 已完成，路径 `specs/006-real-harness-pipeline/spec.md`。
- `clarify`: 当前规格无开放澄清；本计划记录为无阻塞歧义，但应在后续正式 `$speckit-clarify` 中回填。
- `checklist`: 已有 `specs/006-real-harness-pipeline/checklists/requirements.md`，18/18 通过；建议后续专项 checklist 覆盖安全/部署审批。
- `plan`: 本文件。
- `tasks`: 计划生成 `specs/006-real-harness-pipeline/tasks.md`。
- `implement`: 仅在 `tasks.md` 生成并检查清单通过后开始。

## Phase 0 Research

研究输出见 [research.md](./research.md)。

## Phase 1 Design

设计输出：

- [data-model.md](./data-model.md)
- [contracts/real-harness-pipeline-contract.md](./contracts/real-harness-pipeline-contract.md)
- [contracts/openapi.yaml](./contracts/openapi.yaml)
- [quickstart.md](./quickstart.md)

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| 暂未执行独立 clarify 命令 | 用户本轮直接要求 plan/tasks/implement，且 spec 无开放澄清 | 直接阻断会中断用户要求；本计划显式记录回填需求，并在 implement 前使用 checklist 门禁 |
| 功能范围较大，包含模型、流水线、安全、部署、沙箱 | 用户明确要求将 Harness 推进到真实端到端自主交付能力 | 只做模型 provider 无法验证交付物 A 自动打造交付物 B 的主线目标 |
