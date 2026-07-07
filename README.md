# AI 自主开发系统 + OpenClaw Skill 线上商店

## 项目定位

本项目用于建设一个长运行 AI 自主开发调度系统（Agent Dev Harness），并以
OpenClaw Skill 线上商店作为标准业务案例，验证 AI 从需求拆解、架构设计、代码生成、
自测、构建到部署的全链路自主开发能力。

## 交付物

- 交付物 A：AI 自主开发系统 Demo，包括智能体调度、任务持久化、断点续跑、工具调用、
  自测、打包和部署流程。
- 交付物 B：由交付物 A 自动生成并部署的 OpenClaw Skill 线上商店 Web Demo。
- 交付物 C：项目统一规范与原则文档，包括宪章、规格、计划、任务、测试和部署文档。

## 治理原则摘要

- 可观测优先：所有 Agent 行为、工具调用、测试、构建和部署记录必须持久化。
- 最小人工干预：仅在需求歧义、高危操作、权限/支付/隐私/安全配置和无法自动修复的阻断问题上请求人工确认。
- 分层解耦：Agent Dev Harness 与 Skill 商店业务代码必须隔离。
- 安全左移：生成、评审、构建、发布全链路执行安全检查。
- 标准化闭环：需求、架构、实现、测试、扫描、构建、部署和报告必须形成自动化闭环。
- 中文优先：项目文档默认使用中文。
- 增量提交：每完成一个可独立验证的小功能、开发修改或修复，必须更新本 README 并提交到 GitHub。
- Spec Kit 严格流程：每次开发、修改或修复必须按 `specify → clarify → checklist → plan → tasks → implement` 顺序执行。

## 目录结构

```text
.specify/
├── memory/constitution.md      # 项目宪章
├── templates/                  # Spec Kit 模板
├── scripts/                    # Spec Kit 脚本
└── workflows/                  # 工作流配置
README.md                       # 项目入口、开发流程和变更记录
harness/                        # Agent Dev Harness 后端调度系统
skill-store/                    # OpenClaw Skill 商店前后端骨架
```

`skill-store/` 内部按 `backend/`、`frontend/`、`infra/` 和 `docs/` 分层，商城业务与
Agent Dev Harness 保持独立边界。

## 开发流程

1. `speckit-specify`：创建或更新功能规格。
2. `speckit-clarify`：澄清需求；如无待澄清问题，也必须记录无歧义结论。
3. `speckit-checklist`：生成并通过需求检查清单。
4. `speckit-plan`：创建技术实现计划、研究记录、数据模型、契约和 quickstart。
5. `speckit-tasks`：生成可执行任务清单。
6. `speckit-implement`：严格按 `tasks.md` 执行实现、验证和任务状态更新。
7. 每个小功能独立实现、独立验证、独立记录。
8. 修改完成后更新本 README 的开发记录。
9. 运行对应测试、扫描或文档校验。
10. 使用规范提交信息提交并推送到 GitHub。
11. 如果任一步骤、当前 Git 或 GitHub 环境不可用，必须在开发记录和对应 Spec 产物中说明阻塞原因、替代验证和补齐计划。

## 提交规范

提交信息格式：

```text
[模块] 动作：内容
```

示例：

```text
[docs] 新增：中文优先文档规范与README开发记录
[harness] 修复：任务断点恢复状态丢失问题
[skill-goods] 新增：Skill版本更新接口
```

## 运行方式

Agent Dev Harness 后端：

```bash
cd harness/backend
mvn spring-boot:run
```

OpenClaw Skill 商店后端：

```bash
cd skill-store/backend
mvn spring-boot:run
```

OpenClaw Skill 商店前端：

```bash
cd skill-store/frontend
npm install
npm run dev
```

## 测试与部署

Agent Dev Harness 验证：

```bash
cd harness/backend
mvn verify -Pverification
```

OpenClaw Skill 商店验证：

```bash
cd skill-store/backend
mvn verify

cd ../frontend
npm test
npm run build
npm audit --audit-level=high
```

部署脚本和云端发布仍处于占位阶段，真实端口开放、权限配置、数据库初始化和线上环境销毁
必须先触发人工确认。

## 开发记录

| 日期 | 类型 | 范围 | 说明 | 验证结果 | GitHub 状态 |
|------|------|------|------|----------|-------------|
| 2026-07-07 | 澄清 | Agent Dev Harness | 对“完善 Harness 自主开发能力”规格执行 clarify，确认范围、风险门禁、失败重试、交付物 A/B 边界和执行器阶段性策略无阻塞歧义 | 未发现需要人工决策的关键歧义；规格 Clarifications 已记录；需求清单仍全部通过 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 规格 | Agent Dev Harness | 创建“完善 Harness 自主开发能力”规格，覆盖任务编排、Spec Kit 流程执行器、代码生成、测试执行、日志持久化、失败重试、Git 提交和报告生成 | 规格质量清单已通过；无待澄清标记；clarify 已完成，等待后续 checklist/plan/tasks/implement | 已提交并推送：`078d345` |
| 2026-07-07 | 实现 | OpenClaw Skill 商店交易闭环 | 按 Spec Kit 流程新增注册、登录、创作者上传、管理员审核、买家下单、模拟支付和已购授权演示闭环 | `mvn verify` 通过 10 个后端测试；`npm test` 通过 7 个前端测试；`npm run build` 通过；`npm audit --audit-level=high` 零漏洞；未发现真实密钥或真实支付配置 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 治理 | Spec Kit 开发流程 | 将每次开发、修改或修复必须严格执行 `specify → clarify → checklist → plan → tasks → implement` 写入宪章、Spec/Plan/Tasks 模板和 README | 已更新宪章到 v1.2.0；宪章占位符检查无残留；模板已同步 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 文档 | OpenClaw Skill 商店 | 根据已执行的商城骨架实现补齐 `002-init-skill-store` 的 plan、research、data-model、contracts、quickstart 和 tasks，并将已完成任务追溯标记 | `setup-plan.ps1` 与 `setup-tasks.ps1` 可识别当前功能目录和设计产物；占位符检查无残留 | 已提交并推送：`43ae66c` |
| 2026-07-07 | 实现 | OpenClaw Skill 商店 | 初始化 Java Spring Boot + Vue 3 前后端分离工程骨架，包含模块化单体后端、三角色前端入口、Flyway 基础表、Docker/Compose 占位、中文文档和安全说明 | 后端 `mvn test` 通过 8 个测试；前端 `npm test` 通过 5 个测试；`npm run build` 通过；`npm audit --audit-level=high` 零漏洞 | 随本次提交推送到 `origin/master` |
| 2026-07-06 | 规格 | OpenClaw Skill 商店 | 创建“初始化 OpenClaw Skill 商店前后端工程骨架”功能规格、质量清单和 Spec Kit feature 指针 | 已完成规格质量清单；无待澄清标记 | 已提交并推送：`0a9661b` |
| 2026-07-06 | 实现 | Agent Dev Harness | 实现 Java Spring Boot 后端骨架、模块注册、任务状态、执行事件、人工门禁、工具插件、报告、文档、Docker/脚本占位和运行产物目录 | 已按 TDD 先红后绿；`mvn verify -Pverification` 通过 14 个测试；README 和 Harness 文档脚本检查通过；未发现密钥模式命中 | 已提交并推送：`ba11a46` |
| 2026-07-06 | 任务 | Agent Dev Harness | 生成工程骨架实现任务清单，按 Setup、Foundational、US1、US2、US3 和 Polish 分阶段拆解 | 已生成可执行任务；任务包含测试、文档、质量门禁、README 与 GitHub 提交要求 | 已提交并推送：`5bdf710` |
| 2026-07-06 | 计划 | Agent Dev Harness | 基于 Java Spring Boot + Vue 3/React 技术栈生成工程骨架实施计划、研究记录、数据模型、REST/CLI 合约和 quickstart | 已完成计划产物；无宪章违规；前端控制台后置为可选扩展 | 随本次提交推送到 `origin/master` |
| 2026-07-06 | 规格 | Agent Dev Harness | 创建“初始化 Agent Dev Harness 工程骨架”功能规格、质量清单和 Spec Kit feature 指针 | 已完成规格质量清单；无待澄清标记 | 随本次提交推送到 `origin/master` |
| 2026-07-06 | 初始化 | 文档治理 | 初始化 README，并将中文优先、README 记录、增量 GitHub 提交写入项目治理规则 | 已更新宪章和 Spec Kit 模板；已检查宪章占位符 | 已提交并推送：`ce0fe90` |

## 待办事项

- 补充 OpenClaw Skill 商店真实注册、登录、下单、支付模拟、上传和审核闭环。
- 补充商城 E2E、安全扫描、Docker 镜像构建和冒烟部署流水线。
- 将部署占位脚本接入可回滚的演示环境发布流程。
