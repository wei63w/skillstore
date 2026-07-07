# Implementation Plan: 真实模型执行与 Harness Console

**Branch**: `007-real-harness-console` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/007-real-harness-console/spec.md`

## Summary

本功能把 Agent Dev Harness 从 dry-run 骨架推进到“可本地真实运行”的第一版：后端接入 DeepSeek/OpenAI/Claude/Codex 真实代码模型 provider，模型输出必须通过 PatchPlan JSON Schema 校验和 diff 审阅；流水线真实执行代码生成、测试、SAST/依赖扫描、Docker 构建和受控 Git 提交；新增轻量 Vue 3 Harness Console，提供任务列表、阶段时间线、日志/审批/diff 三栏操作界面；通过 Docker Compose 启动 Harness 后端和控制台。

**Documentation Language**: 所有项目文档默认中文。仅代码标识符、第三方协议、API 字段和英文专有名词使用英文。

## Technical Context

**Language/Version**: Java 21、Spring Boot 3.3.7、Vue 3、TypeScript、Vite

**Primary Dependencies**: Spring Web、Spring Validation、Actuator、Jackson、JUnit 5、Vue Router、Pinia、Axios、Vitest

**Storage**: 本阶段沿用文件持久化：`harness/runtime/**` 存储 run、日志、报告、diff、审批和模型调用证据；不引入数据库。

**Testing**: 后端 `mvn test` / `mvn verify`；前端 `npm test` / `npm run build`；安全扫描以本地命令执行器封装。

**Target Platform**: 本地 Windows/Unix 开发环境与 Docker Compose 演示环境。

**Project Type**: Web service + Vue console + Docker Compose。

**Performance Goals**: 单次模型生成阶段在 provider 正常响应时目标不超过 5 分钟；断点恢复读取 runtime 状态不超过 30 秒；最多同时追踪 3 条 pipeline。

**Constraints**: 不写入明文密钥；真实 provider 未配置 key 时必须阻断而不是降级泄露；高危部署、权限、凭据、隐私、安全相关动作必须进入审批。

**Scale/Scope**: 覆盖 Harness 单实例本地演示；Skill Store 作为生成/修改目标工程，不把商城业务规则写入 Harness 核心。

## Constitution Check

*GATE: Passed before Phase 0 research; re-checked after Phase 1 design.*

- **Observability & Reproducibility**: 模型调用、Schema 校验、diff、命令输出、扫描报告、Docker/Git 结果均持久化到 runtime，可回放和断点读取。
- **Human Risk Gates**: 缺失密钥、非法路径、高危扫描、部署/端口/权限/Git push 等动作通过审批或阻断状态表达。
- **Harness/Business Decoupling**: Harness 只读取 spec/plan/tasks/contracts 和目标路径白名单，不内嵌 OpenClaw Skill Store 业务逻辑。
- **Security Shift-Left**: 模型输出先 Schema 校验，再路径/敏感内容扫描，再 diff 审阅；SAST/依赖扫描在 Docker 构建前执行。
- **Automation Loop**: 覆盖模型生成、补丁审阅、应用、测试、扫描、构建、Git、报告、控制台查看。
- **Testing Gates**: 后端单元/接口测试、前端组件/构建测试、命令执行器测试、控制台渲染测试均纳入任务。
- **Performance Baselines**: 本地运行目标记录在 Technical Context，provider 延迟由报告记录。
- **Deployment & Rollback**: Compose 只做本地演示部署；线上发布和回滚仍需后续高危审批执行器。
- **Documentation**: 本功能输出 research、data-model、contracts、quickstart、README 开发记录和实现报告。
- **README & GitHub Records**: plan/tasks/implement 每个可验证切片更新 README 并提交推送。
- **Spec Kit Workflow Gate**: `specify` 与 `checklist` 已完成；本轮补齐 `plan`、`tasks`、`implement`。本功能未发现 `[NEEDS CLARIFICATION]`，因此 clarify 结论记录为“无阻塞澄清项”，后续若出现 provider、部署或权限歧义必须回补 clarify。

## Project Structure

### Documentation (this feature)

```text
specs/007-real-harness-console/
├── spec.md
├── plan.md
├── checklists/
│   └── requirements.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── openapi.yaml
│   └── real-provider-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
harness/
├── backend/
│   ├── Dockerfile
│   └── src/main/java/com/openclaw/harness/
│       ├── api/
│       ├── model/
│       ├── pipeline/
│       ├── security/
│       ├── executors/
│       └── reports/
├── frontend/
│   ├── Dockerfile
│   ├── package.json
│   └── src/
│       ├── api/
│       ├── components/
│       ├── stores/
│       ├── App.vue
│       └── main.ts
└── runtime/

docker-compose.yml
```

**Structure Decision**: 后端沿用既有 Spring Boot Harness 模块；新增 `harness/frontend` Vue 3 控制台；根目录 Compose 编排前后端。Skill Store 保持目标工程身份，不移动目录。

## Spec Kit Workflow Evidence

- `specify`: 已完成，见 `specs/007-real-harness-console/spec.md`
- `clarify`: 当前规格无 `[NEEDS CLARIFICATION]`；真实密钥、端口开放、部署权限、Git push 均按高危审批处理，不作为实现前阻塞项
- `checklist`: 已完成，见 `specs/007-real-harness-console/checklists/requirements.md`，18/18 通过
- `plan`: 本文件
- `tasks`: 计划生成 `specs/007-real-harness-console/tasks.md`
- `implement`: 任务生成后执行

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| 无 | N/A | N/A |

## Phase 0: Research

见 [research.md](./research.md)。

## Phase 1: Design

见 [data-model.md](./data-model.md)、[contracts/openapi.yaml](./contracts/openapi.yaml)、[contracts/real-provider-contract.md](./contracts/real-provider-contract.md)、[quickstart.md](./quickstart.md)。

## Phase 2: Task Planning Approach

任务按独立用户故事生成：先完成真实 provider 与 Schema 校验，再完成真实 pipeline，再完成 Vue Console，最后补齐 Docker Compose、文档、测试和提交记录。每个切片都要更新 README 开发记录并提交。
