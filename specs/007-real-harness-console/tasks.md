# Tasks: 真实模型执行与 Harness Console

**Input**: Design documents from `specs/007-real-harness-console/`

**Prerequisites**: `plan.md`、`spec.md`、`research.md`、`data-model.md`、`contracts/`、`quickstart.md`

**Spec Kit Workflow Gate**: `specify` 已完成；`clarify` 结论为无阻塞澄清项；`checklist` 18/18 通过；`plan` 已生成并提交；本文件用于 `implement`。

**Documentation Language**: 默认中文；代码标识符、API 字段、第三方协议名保留英文。

**Tests**: 后端单元/接口测试、前端组件/构建测试、SAST/依赖扫描、Compose smoke 验证均为必做。

## Format: `[ID] [P?] [Story] Description`

- **[P]**: 可并行执行
- **[Story]**: US1/US2/US3/US4
- 每个任务包含明确文件路径

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: 准备共享配置、目录和测试入口。

- [ ] T001 创建 `harness/frontend/` Vue 3 控制台目录骨架，包含 `package.json`、`index.html`、`vite.config.ts`、`tsconfig.json`
- [ ] T002 [P] 新增 `harness/backend/src/main/resources/application.yml` 中真实 provider 配置占位，使用环境变量名不写明文密钥
- [ ] T003 [P] 新增 `harness/backend/src/test/resources/application-test.yml` 中 stub/deepseek/openai/claude/codex 测试配置占位
- [ ] T004 [P] 新增或更新 `harness/backend/src/test/java/com/openclaw/harness/model/` provider 单元测试目录
- [ ] T005 [P] 新增 `harness/frontend/src/` 的 api、stores、components、styles 基础目录
- [ ] T006 更新 `README.md` 开发记录，记录 tasks 生成结果
- [ ] T007 提交并推送任务清单，commit message 为 `[harness] 新增：真实模型控制台任务清单`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: 用户故事共用的模型、命令、报告和控制台 API 基础。

- [ ] T008 在 `harness/backend/src/main/java/com/openclaw/harness/model/` 新增 provider 协议枚举和配置模型，覆盖 OpenAI Chat、OpenAI Responses、Anthropic Messages、Stub
- [ ] T009 在 `harness/backend/src/main/java/com/openclaw/harness/model/` 重构 provider registry，使其从配置读取 enabled provider 并暴露状态
- [ ] T010 在 `harness/backend/src/main/java/com/openclaw/harness/executors/CommandExecutor.java` 增强命令输出脱敏、超时和工作目录校验
- [ ] T011 在 `harness/backend/src/main/java/com/openclaw/harness/pipeline/` 增加 stage evidence 日志/报告路径字段或视图模型
- [ ] T012 在 `harness/backend/src/main/java/com/openclaw/harness/api/` 暴露 provider 状态、pipeline list/detail/logs API
- [ ] T013 [P] 在 `harness/backend/src/test/java/com/openclaw/harness/pipeline/` 增加 run 列表、详情、日志读取测试
- [ ] T014 [P] 在 `harness/backend/src/test/java/com/openclaw/harness/executors/` 增加命令执行器脱敏和失败分类测试

**Checkpoint**: Foundation ready - provider 和 pipeline API 可被控制台复用。

---

## Phase 3: User Story 1 - 使用真实代码模型生成补丁 (Priority: P1) 🎯 MVP

**Goal**: DeepSeek/OpenAI/Claude/Codex 可配置真实调用，输出 PatchPlan JSON 并通过 Schema 校验。

**Independent Test**: 缺失 key 时结构化阻断；mock HTTP 成功时解析 PatchPlan；非法 JSON 时阻断且不应用。

### Tests for User Story 1

- [ ] T015 [P] [US1] 在 `harness/backend/src/test/java/com/openclaw/harness/model/RealHttpCodeModelProviderTest.java` 测试缺失密钥阻断且不泄露密钥
- [ ] T016 [P] [US1] 在同一测试中覆盖 DeepSeek/OpenAI Chat 响应解析为 PatchPlan
- [ ] T017 [P] [US1] 在同一测试中覆盖 OpenAI Responses 响应解析为 PatchPlan
- [ ] T018 [P] [US1] 在同一测试中覆盖 Claude Messages 响应解析为 PatchPlan
- [ ] T019 [P] [US1] 在 `harness/backend/src/test/java/com/openclaw/harness/model/ModelProviderStatusControllerTest.java` 测试 provider 状态 API

### Implementation for User Story 1

- [ ] T020 [US1] 在 `harness/backend/src/main/java/com/openclaw/harness/model/HttpCodeModelProvider.java` 实现真实 HTTP 调用、重试、超时和脱敏错误
- [ ] T021 [US1] 在同文件实现 DeepSeek/OpenAI Chat request body 与 `choices[0].message.content` 解析
- [ ] T022 [US1] 在同文件实现 OpenAI Responses request body 与 output text 解析
- [ ] T023 [US1] 在同文件实现 Claude Messages request body 与 content text 解析
- [ ] T024 [US1] 在 `harness/backend/src/main/java/com/openclaw/harness/model/ModelOutputSchemaValidator.java` 强化 JSON 提取失败和 Schema 失败信息
- [ ] T025 [US1] 在 `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java` 记录模型调用 evidence 和 provider 错误类别
- [ ] T026 [US1] 更新 `README.md` 开发记录并提交 `[harness] 新增：真实模型调用`

**Checkpoint**: 真实 provider 可独立测试，缺少 key 不泄密。

---

## Phase 4: User Story 2 - 执行真实流水线与安全扫描 (Priority: P2)

**Goal**: 非 dry-run pipeline 能真实执行代码生成、补丁审阅、测试、扫描、Docker build、Git 提交候选和报告。

**Independent Test**: 对小型 feature 运行 pipeline，阶段状态与命令输出真实持久化；高危扫描阻断构建。

### Tests for User Story 2

- [ ] T027 [P] [US2] 在 `harness/backend/src/test/java/com/openclaw/harness/pipeline/PipelineServiceRealExecutionTest.java` 测试成功命令阶段
- [ ] T028 [P] [US2] 在同一测试中覆盖测试失败阻断后续构建
- [ ] T029 [P] [US2] 在同一测试中覆盖 SAST 高危发现阻断 Docker 构建
- [ ] T030 [P] [US2] 在 `harness/backend/src/test/java/com/openclaw/harness/security/SastScanExecutorTest.java` 测试敏感模式和危险调用扫描
- [ ] T031 [P] [US2] 在 `harness/backend/src/test/java/com/openclaw/harness/security/DependencyScanExecutorTest.java` 测试 Maven/npm 扫描命令计划和工具缺失分类

### Implementation for User Story 2

- [ ] T032 [US2] 在 `harness/backend/src/main/java/com/openclaw/harness/security/` 新增 SAST 扫描执行器，输出风险等级和证据文件
- [ ] T033 [US2] 在 `harness/backend/src/main/java/com/openclaw/harness/security/` 新增依赖漏洞扫描执行器，封装 Maven/npm audit
- [ ] T034 [US2] 在 `harness/backend/src/main/java/com/openclaw/harness/pipeline/PipelineService.java` 实现非 dry-run 阶段串联和失败短路
- [ ] T035 [US2] 在 `PipelineService` 接入三轮修复决策，超限后创建人工审批/介入请求
- [ ] T036 [US2] 在 `PipelineService` 中持久化每阶段日志、命令、退出码、summary 和 artifact path
- [ ] T037 [US2] 在 `harness/backend/src/main/java/com/openclaw/harness/executors/GitSubmitExecutor.java` 增强本地 commit 候选，高危 push 仍需审批
- [ ] T038 [US2] 更新 `README.md` 开发记录并提交 `[harness] 新增：真实流水线执行`

**Checkpoint**: 后端 pipeline 能真实执行本地命令并输出可回放证据。

---

## Phase 5: User Story 3 - 使用轻量 Harness Console 操作流水线 (Priority: P3)

**Goal**: Vue 3 控制台提供左任务列表、中阶段、右日志/审批/diff 工作界面。

**Independent Test**: 启动控制台后能显示 provider/run 列表，发起 dry-run，查看阶段和日志。

### Tests for User Story 3

- [ ] T039 [P] [US3] 在 `harness/frontend/src/App.test.ts` 测试三栏布局渲染
- [ ] T040 [P] [US3] 在 `harness/frontend/src/api/harnessApi.test.ts` 测试 API 封装和错误提示
- [ ] T041 [P] [US3] 在 `harness/frontend/src/stores/pipelineStore.test.ts` 测试 run 选择、刷新和启动 pipeline 状态
- [ ] T042 [P] [US3] 运行 `harness/frontend` 构建测试，确认 TypeScript 无错误

### Implementation for User Story 3

- [ ] T043 [US3] 在 `harness/frontend/src/api/harnessApi.ts` 实现 provider、run、logs、approval、start pipeline 请求封装
- [ ] T044 [US3] 在 `harness/frontend/src/stores/pipelineStore.ts` 实现任务列表、选中 run、阶段、日志、审批状态
- [ ] T045 [US3] 在 `harness/frontend/src/App.vue` 实现三栏工作台，默认展示真实可操作界面
- [ ] T046 [US3] 在 `harness/frontend/src/styles.css` 实现响应式布局、状态标签、日志面板和 diff 面板
- [ ] T047 [US3] 在 `harness/backend/src/main/java/com/openclaw/harness/api/` 确保 CORS 支持本地控制台端口
- [ ] T048 [US3] 更新 `README.md` 开发记录并提交 `[harness-console] 新增：控制台界面`

**Checkpoint**: 控制台本地可访问并操作 Harness API。

---

## Phase 6: User Story 4 - 使用 Docker Compose 本地部署前后端 (Priority: P4)

**Goal**: 一个 Compose 命令启动 Harness 后端和 Vue 控制台，runtime 持久化，密钥走环境变量。

**Independent Test**: `docker compose config` 通过；本地服务配置无明文密钥；Dockerfile 可构建。

### Tests for User Story 4

- [ ] T049 [P] [US4] 验证 `docker compose config` 通过且不包含明文密钥
- [ ] T050 [P] [US4] 验证 `harness/backend/Dockerfile` 构建命令和健康检查路径
- [ ] T051 [P] [US4] 验证 `harness/frontend/Dockerfile` 产物由 Nginx 或静态服务提供

### Implementation for User Story 4

- [ ] T052 [US4] 新增 `harness/backend/Dockerfile`
- [ ] T053 [US4] 新增 `harness/frontend/Dockerfile`
- [ ] T054 [US4] 新增根目录 `docker-compose.yml`，包含 backend、frontend、runtime volume、端口和环境变量占位
- [ ] T055 [US4] 新增 `.dockerignore` 或服务级 ignore，避免打包 runtime、node_modules、target
- [ ] T056 [US4] 更新 `README.md` 与 `specs/007-real-harness-console/quickstart.md` 的 Compose 启动说明
- [ ] T057 [US4] 提交 `[harness] 新增：Compose部署`

---

## Phase 7: Polish & Cross-Cutting

**Purpose**: 验证、报告、任务状态和最终提交。

- [ ] T058 [P] 运行 `cd harness/backend && mvn test`
- [ ] T059 [P] 运行 `cd harness/backend && mvn verify`
- [ ] T060 [P] 运行 `cd harness/frontend && npm test`
- [ ] T061 [P] 运行 `cd harness/frontend && npm run build`
- [ ] T062 [P] 运行仓库明文密钥扫描，确认不包含用户提供的真实 API Key
- [ ] T063 更新 `specs/007-real-harness-console/tasks.md`，将已完成任务标记为 `[x]`
- [ ] T064 新增或更新 `specs/007-real-harness-console/implementation-report.md`，记录实现、验证、风险和后续限制
- [ ] T065 更新 `README.md` 最终开发记录并提交 `[harness] 完成：真实模型控制台流水线`
