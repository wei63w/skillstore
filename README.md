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
| 2026-07-07 | 修复 | Harness Console | 修复控制台 Provider 下拉为空问题：前端默认 API baseURL 改为 `http://localhost:8080`，避免 5174 静态服务未代理 `/api` 时请求 404，并新增 API baseURL 回归测试 | `npm test` 通过 2 个前端测试；`npm run build` 通过；`http://localhost:8080/api/harness/model-providers` 正常返回 provider 列表 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness / Harness Console | 完成真实模型执行与轻量控制台第一版：新增 DeepSeek/OpenAI/Claude/Codex HTTP provider、provider 状态 API、真实 pipeline 命令阶段、SAST 基础敏感扫描、pipeline list/detail/logs API、Vue 3 三栏控制台、Dockerfile 和 Compose 部署配置 | 后端 `mvn test` / `mvn verify` 通过；前端 `npm test` / `npm run build` 通过；`npm audit --audit-level=high` 0 漏洞；`docker compose config` 通过；未发现用户提供的明文 DeepSeek API Key | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 任务 | Agent Dev Harness / Harness Console | 为“真实模型执行与 Harness Console”生成 `tasks.md`，按 Setup、Foundation、US1 真实模型 provider、US2 真实流水线、安全扫描、US3 Vue 控制台、US4 Compose 部署和最终验证拆分任务 | 任务清单共 65 条；格式包含 checkbox、ID、Story 标签和明确文件路径；MVP 为 US1 真实模型调用与 PatchPlan Schema 校验 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 计划 | Agent Dev Harness / Harness Console | 为“真实模型执行与 Harness Console”生成技术计划、研究记录、数据模型、真实 provider 契约、Console API 契约和 quickstart，明确 DeepSeek/OpenAI/Claude/Codex 协议适配、PatchPlan Schema、真实流水线、SAST/依赖扫描、Vue 控制台和 Compose 部署边界 | `check-prerequisites.ps1 -Json` 通过，识别 `research.md`、`data-model.md`、`contracts/`、`quickstart.md`；规格无待澄清标记，clarify 结论记录为无阻塞项 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 规格 | Agent Dev Harness / Harness Console | 创建“真实模型执行与 Harness Console”规格，覆盖 DeepSeek/OpenAI/Claude/Codex 真实调用、真实流水线执行、真实 SAST/漏洞扫描、Vue 3 控制台三栏界面和 Docker Compose 前后端部署 | 规格质量清单 18/18 通过；无待澄清标记；等待后续 clarify/checklist/plan/tasks/implement | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 修改 | Agent Dev Harness | 为测试配置增加 DeepSeek 模型 provider 占位，按官方文档使用 `deepseek-v4-pro`、`https://api.deepseek.com/chat/completions` 和 `DEEPSEEK_API_KEY` 环境变量引用；未写入明文 API Key | `mvn test` 通过 69 个后端测试；仓库扫描未发现用户提供的明文 key；配置仅保存环境变量名 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成真实模型端到端流水线第一版：新增模型 provider 配置占位、fake HTTP provider、模型输出 Schema 校验、补丁 diff review、pipeline dry-run、自动修复决策、安全扫描计划、Docker build dry-run、部署 dry-run、权限沙箱、审批请求和 REST 入口 | `mvn test` 通过 69 个后端测试；`mvn verify` 通过；`mvn dependency:tree -Dscope=runtime` 通过；真实密钥未写入仓库；部署保持 dry-run/审批 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 任务 | Agent Dev Harness | 为“真实模型驱动 Harness 端到端流水线”生成 `tasks.md`，按 Setup、Foundation、US1 真实模型配置与 diff review、US2 端到端 dry-run pipeline、US3 安全构建部署、US4 沙箱审批和 Polish 拆分 | 任务清单共 60 条；格式校验 0 异常；MVP 为 US1，后续按用户故事增量实现和提交 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 计划 | Agent Dev Harness | 为“真实模型驱动 Harness 端到端流水线”生成技术计划、研究记录、数据模型、API/配置契约和 quickstart，明确真实模型 provider、Schema 校验、diff review、pipeline、sandbox、安全扫描、Docker/部署 dry-run 的实现边界 | `check-prerequisites.ps1 -Json` 通过，识别 `research.md`、`data-model.md`、`contracts/`、`quickstart.md`；计划记录 clarify/checklist 回填要求与宪章复杂度说明 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 规格 | Agent Dev Harness | 创建“真实模型驱动 Harness 端到端流水线”规格，覆盖真实代码模型 provider、配置占位、模型输出结构校验、补丁 diff 审阅、自动修复循环、真实 Skill Store 代码修改、E2E/SAST/依赖扫描、容器构建、部署执行器、权限沙箱和高危审批 | 规格质量清单 18/18 通过；无待澄清标记；等待后续 clarify/checklist/plan/tasks/implement | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成 CodeGenerationExecutor 多模型代码生成骨架：新增 `CodeModelProvider` 抽象、供应商注册表、受控 `PatchPlan`、路径白名单、敏感风险扫描、dry-run/apply、测试/文档候选和代码生成报告 | `mvn test` 通过 49 个后端测试；覆盖供应商选择、缺失供应商阻断、上下文加载、补丁计划、路径越界阻断、敏感内容阻断、dry-run 不落盘、apply 受控写入、REST 合约和报告持久化 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 新增 `/api/harness/code-generation` REST 入口，外部控制台或 Harness 流水线可提交 feature/spec/plan/tasks/contracts 触发受控代码生成 | 已新增 REST 合约测试和报告持久化测试；默认模型供应商为 `stub`，真实 OpenAI/Claude/Gemini/local provider 后续通过统一接口接入 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 任务 | Agent Dev Harness | 为“完善 Harness CodeGenerationExecutor”生成 `tasks.md`，拆分多模型抽象、补丁计划、安全应用、测试/文档候选和闭环报告任务 | 任务清单共 44 条；格式包含 checkbox、ID、Story 标签和明确文件路径；MVP 为 US1+US2 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 计划 | Agent Dev Harness | 为“完善 Harness CodeGenerationExecutor”生成技术计划、研究记录、数据模型、代码生成契约、REST 契约和 quickstart | 计划覆盖多供应商 `CodeModelProvider`、受控补丁计划、dry-run/apply、路径与敏感扫描；无宪章违规 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 澄清 | Agent Dev Harness | 对“完善 Harness CodeGenerationExecutor”补充澄清：自动生成可运行代码必须接入代码/推理型大模型并支持多家供应商，多模态模型后置 | 规格 Clarifications 已记录；requirements checklist 仍 16/16 通过；无待澄清标记 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 规格 | Agent Dev Harness | 创建“完善 Harness CodeGenerationExecutor”规格，定义根据 spec/plan/tasks/contracts 生成或修改 skill-store 前后端代码、测试和文档的能力边界 | 规格质量清单 16/16 通过；无待澄清标记；等待后续 clarify/checklist/plan/tasks/implement | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 验证 | Agent Dev Harness | 完成“完善 Harness 自主开发能力”最终验证，补充测试、构建、安全/依赖检查、quickstart、契约覆盖和最终实现报告 | `mvn test` 通过 38 个测试；`mvn verify` 通过；`mvn dependency:tree -Dscope=runtime` 通过；REST/CLI 契约和中文文档检查通过；未发现商城业务规则写入 Harness 核心 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成自主开发能力 US4：新增 README 开发记录更新器、提交前风险扫描、提交信息校验、Git 提交执行器和 git-submit REST API | `mvn test` 通过 38 个后端测试；覆盖 README 记录、敏感路径/明文密钥扫描、临时 Git 仓库本地提交和 git-submit REST 合约 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成自主开发能力 US3：新增工具调用记录、代码差异摘要、产物索引、交付报告模型/服务和 workflow 报告 REST API | `mvn test` 通过 34 个后端测试；覆盖工具调用持久化、产物索引、报告汇总和 report REST 合约 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成自主开发能力 US2：新增 `tasks.md` 解析、任务执行计划、受控代码生成、测试 profile 执行映射、三轮重试策略和失败升级人工门禁 | `mvn test` 通过 30 个后端测试；覆盖任务解析、代码生成产物、测试 profile、三轮重试上限、人工门禁升级和 run-tasks REST 合约 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 实现 | Agent Dev Harness | 完成自主开发能力 US1：新增 Spec Kit 流水线编排、阶段产物校验、checkpoint 持久化、恢复服务、阶段查询 REST API 和 CLI 文档 | `mvn test` 通过 24 个后端测试；覆盖阶段顺序、缺失产物阻断、30 秒内恢复目标、REST start/resume/phases 合约和 CLI 合约 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 任务 | Agent Dev Harness | 为“完善 Harness 自主开发能力”生成 `tasks.md`，按 Setup、Foundation、US1-US4 和最终校验拆分 88 条可执行任务 | 任务格式校验通过；全部任务包含 checkbox、ID、必要 Story 标签和明确文件路径；MVP 范围为 US1 Spec Kit 流水线编排 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 计划 | Agent Dev Harness | 为“完善 Harness 自主开发能力”生成技术实现计划、研究记录、数据模型、CLI/REST 契约和 quickstart 验证指南 | `check-prerequisites.ps1 -Json` 通过，识别 `research.md`、`data-model.md`、`contracts/`、`quickstart.md`；无未解决澄清项；agent context 更新脚本在当前项目中不存在，已按工具现状跳过 | 随本次提交推送到 `origin/master` |
| 2026-07-07 | 清单 | Agent Dev Harness | 为“完善 Harness 自主开发能力”生成 Harness 流水线需求质量清单，覆盖 Spec Kit 编排、代码/测试执行器、可观测性、失败恢复和 GitHub 证据 | `harness-workflow.md` 27/27 通过；当前 `check-prerequisites.ps1 -Json` 在 plan 前提示缺少 plan，与宪章顺序存在脚本约束差异，已按 checklist 阶段基于 spec 生成 | 随本次提交推送到 `origin/master` |
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
