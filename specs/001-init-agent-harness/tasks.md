# Tasks: 初始化 Agent Dev Harness 工程骨架

**Input**: Design documents from `/specs/001-init-agent-harness/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Documentation Language**: 本任务文件和后续项目文档默认使用中文。仅代码标识符、
第三方协议、API 字段和必要英文专有名词使用英文。

**Tests**: 本功能要求测试。后端单元测试、集成测试、REST 契约验证、CLI 验证、依赖扫描
和冒烟验证必须进入任务。前端 E2E 本阶段不适用，因为 Vue 3/React 控制台只预留边界。

**Organization**: 任务按用户故事组织，保证每个故事都能独立实现、验证和提交。

## Format: `[ID] [P?] [Story] Description`

- **[P]**: 可并行执行，前提是修改不同文件且不依赖未完成任务。
- **[Story]**: 仅用户故事阶段使用，例如 `[US1]`、`[US2]`、`[US3]`。
- 每个任务描述必须包含明确文件路径。

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: 初始化 Java Spring Boot 后端工程、Harness 顶层目录、运行产物目录和基础文档。

- [X] T001 Create Harness root directories in `harness/backend/`, `harness/console/`, `harness/runtime/`, `harness/runtime/tasks/`, `harness/runtime/logs/`, `harness/runtime/context/`, `harness/runtime/artifacts/`, `harness/runtime/reports/`, `infra/docker/`, and `infra/scripts/`
- [X] T002 Initialize Maven Spring Boot project descriptor in `harness/backend/pom.xml`
- [X] T003 Create Spring Boot application entry class in `harness/backend/src/main/java/com/openclaw/harness/AgentHarnessApplication.java`
- [X] T004 [P] Create base application configuration in `harness/backend/src/main/resources/application.yml`
- [X] T005 [P] Create test application configuration in `harness/backend/src/test/resources/application-test.yml`
- [X] T006 [P] Create Harness backend README in `harness/README.md`
- [X] T007 [P] Create console placeholder README in `harness/console/README.md`
- [X] T008 [P] Create runtime artifact policy document in `harness/backend/docs/runtime-artifacts.md`
- [X] T009 [P] Create Docker placeholder file in `infra/docker/harness-backend.Dockerfile`
- [X] T010 [P] Create local smoke script placeholder in `infra/scripts/harness-smoke.ps1`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: 建立所有用户故事共享的包结构、模型、统一响应、异常处理、日志与测试基础。

**CRITICAL**: 本阶段完成前，不得开始用户故事实现。

- [X] T011 Create package structure under `harness/backend/src/main/java/com/openclaw/harness/agents/`, `api/`, `scheduler/`, `state/`, `context/`, `tools/`, `gates/`, `observability/`, `reports/`, `common/`, and `config/`
- [X] T012 [P] Create unified API response model in `harness/backend/src/main/java/com/openclaw/harness/common/ApiResponse.java`
- [X] T013 [P] Create global exception handler in `harness/backend/src/main/java/com/openclaw/harness/common/GlobalExceptionHandler.java`
- [X] T014 [P] Create validation error response model in `harness/backend/src/main/java/com/openclaw/harness/common/ValidationError.java`
- [X] T015 [P] Create task status enum in `harness/backend/src/main/java/com/openclaw/harness/state/TaskStatus.java`
- [X] T016 [P] Create execution result enum in `harness/backend/src/main/java/com/openclaw/harness/observability/ExecutionResult.java`
- [X] T017 [P] Create risk level enum in `harness/backend/src/main/java/com/openclaw/harness/gates/RiskLevel.java`
- [X] T018 [P] Create human gate status enum in `harness/backend/src/main/java/com/openclaw/harness/gates/HumanGateStatus.java`
- [X] T019 Create structured logging configuration in `harness/backend/src/main/java/com/openclaw/harness/config/ObservabilityConfig.java`
- [X] T020 Create runtime path configuration in `harness/backend/src/main/java/com/openclaw/harness/config/RuntimePathProperties.java`
- [X] T021 Create runtime directory initializer in `harness/backend/src/main/java/com/openclaw/harness/config/RuntimeDirectoryInitializer.java`
- [X] T022 [P] Create base Spring context smoke test in `harness/backend/src/test/java/com/openclaw/harness/AgentHarnessApplicationTests.java`
- [X] T023 [P] Create dependency scan documentation placeholder in `harness/backend/docs/security-scan.md`
- [X] T024 [P] Create coverage report documentation placeholder in `harness/backend/docs/test-coverage.md`

**Checkpoint**: Spring Boot 工程可加载，公共模型、配置和运行产物目录约定可供所有故事使用。

---

## Phase 3: User Story 1 - 查看 Harness 项目入口 (Priority: P1)

**Goal**: 新维护者可从 README、目录结构和健康检查入口理解 Harness 目标、边界和扩展点。

**Independent Test**: 从仓库根目录阅读 README 和 `harness/README.md`，并运行后端健康检查测试，10 分钟内可识别 Harness 与 Skill 商店边界、模块位置和后续扩展点。

### Tests for User Story 1

- [X] T025 [P] [US1] Create health endpoint integration test in `harness/backend/src/test/java/com/openclaw/harness/api/HealthControllerTest.java`
- [X] T026 [P] [US1] Create module registry unit test in `harness/backend/src/test/java/com/openclaw/harness/agents/AgentModuleRegistryTest.java`
- [X] T027 [P] [US1] Create documentation boundary check script in `infra/scripts/check-harness-docs.ps1`

### Implementation for User Story 1

- [X] T028 [P] [US1] Create agent module descriptor model in `harness/backend/src/main/java/com/openclaw/harness/agents/AgentModuleDescriptor.java`
- [X] T029 [P] [US1] Create agent module status enum in `harness/backend/src/main/java/com/openclaw/harness/agents/AgentModuleStatus.java`
- [X] T030 [US1] Create agent module registry service in `harness/backend/src/main/java/com/openclaw/harness/agents/AgentModuleRegistry.java`
- [X] T031 [US1] Register planning, coding, testing, build-deploy, and review module placeholders in `harness/backend/src/main/java/com/openclaw/harness/agents/DefaultAgentModules.java`
- [X] T032 [US1] Create health controller in `harness/backend/src/main/java/com/openclaw/harness/api/HealthController.java`
- [X] T033 [US1] Create module listing controller in `harness/backend/src/main/java/com/openclaw/harness/api/AgentModuleController.java`
- [X] T034 [US1] Update Harness README with module boundary and startup guidance in `harness/README.md`
- [X] T035 [US1] Update root README development log for US1 completion in `README.md`
- [X] T036 [US1] Commit US1 changes to GitHub with message `[harness] 新增：工程入口与模块边界`

**Checkpoint**: US1 可独立演示：健康检查可用，模块占位可查询，文档说明 Harness 与 Skill 商店隔离。

---

## Phase 4: User Story 2 - 验证长运行任务基础约定 (Priority: P2)

**Goal**: 骨架能表达任务状态、执行事件、上下文摘要、人工确认门禁、工具插件和阶段产物约定。

**Independent Test**: 创建一个示例任务，查询状态和事件，模拟人工确认门禁，确认运行产物目录和记录规则可追踪。

### Tests for User Story 2

- [X] T037 [P] [US2] Create task service unit test in `harness/backend/src/test/java/com/openclaw/harness/state/HarnessTaskServiceTest.java`
- [X] T038 [P] [US2] Create task REST contract integration test in `harness/backend/src/test/java/com/openclaw/harness/api/HarnessTaskControllerTest.java`
- [X] T039 [P] [US2] Create execution event service unit test in `harness/backend/src/test/java/com/openclaw/harness/observability/ExecutionEventServiceTest.java`
- [X] T040 [P] [US2] Create human gate service unit test in `harness/backend/src/test/java/com/openclaw/harness/gates/HumanGateServiceTest.java`
- [X] T041 [P] [US2] Create tool plugin registry unit test in `harness/backend/src/test/java/com/openclaw/harness/tools/ToolPluginRegistryTest.java`
- [X] T042 [P] [US2] Create CLI contract smoke test placeholder in `harness/backend/src/test/java/com/openclaw/harness/cli/HarnessCliContractTest.java`

### Implementation for User Story 2

- [X] T043 [P] [US2] Create harness task model in `harness/backend/src/main/java/com/openclaw/harness/state/HarnessTask.java`
- [X] T044 [P] [US2] Create create task request DTO in `harness/backend/src/main/java/com/openclaw/harness/api/dto/CreateTaskRequest.java`
- [X] T045 [P] [US2] Create task response DTO in `harness/backend/src/main/java/com/openclaw/harness/api/dto/TaskResponse.java`
- [X] T046 [US2] Create in-memory task repository in `harness/backend/src/main/java/com/openclaw/harness/state/InMemoryHarnessTaskRepository.java`
- [X] T047 [US2] Create task service in `harness/backend/src/main/java/com/openclaw/harness/state/HarnessTaskService.java`
- [X] T048 [US2] Create task controller for create and status endpoints in `harness/backend/src/main/java/com/openclaw/harness/api/HarnessTaskController.java`
- [X] T049 [P] [US2] Create execution event model in `harness/backend/src/main/java/com/openclaw/harness/observability/ExecutionEvent.java`
- [X] T050 [US2] Create execution event service in `harness/backend/src/main/java/com/openclaw/harness/observability/ExecutionEventService.java`
- [X] T051 [US2] Create task event controller in `harness/backend/src/main/java/com/openclaw/harness/api/TaskEventController.java`
- [X] T052 [P] [US2] Create context snapshot model in `harness/backend/src/main/java/com/openclaw/harness/context/ContextSnapshot.java`
- [X] T053 [P] [US2] Create task artifact model in `harness/backend/src/main/java/com/openclaw/harness/reports/TaskArtifact.java`
- [X] T054 [P] [US2] Create human gate model in `harness/backend/src/main/java/com/openclaw/harness/gates/HumanGate.java`
- [X] T055 [P] [US2] Create gate decision request DTO in `harness/backend/src/main/java/com/openclaw/harness/api/dto/GateDecisionRequest.java`
- [X] T056 [P] [US2] Create human gate response DTO in `harness/backend/src/main/java/com/openclaw/harness/api/dto/HumanGateResponse.java`
- [X] T057 [US2] Create human gate service in `harness/backend/src/main/java/com/openclaw/harness/gates/HumanGateService.java`
- [X] T058 [US2] Create human gate controller in `harness/backend/src/main/java/com/openclaw/harness/api/HumanGateController.java`
- [X] T059 [P] [US2] Create tool plugin descriptor in `harness/backend/src/main/java/com/openclaw/harness/tools/ToolPluginDescriptor.java`
- [X] T060 [US2] Create tool plugin registry in `harness/backend/src/main/java/com/openclaw/harness/tools/ToolPluginRegistry.java`
- [X] T061 [US2] Create CLI command placeholder documentation in `harness/backend/docs/cli.md`
- [X] T062 [US2] Update Harness runtime artifact documentation in `harness/backend/docs/runtime-artifacts.md`
- [X] T063 [US2] Update root README development log for US2 completion in `README.md`
- [X] T064 [US2] Commit US2 changes to GitHub with message `[harness] 新增：长任务状态与门禁约定`

**Checkpoint**: US2 可独立演示：任务、事件、门禁、工具插件和运行产物约定均可通过接口或文档验证。

---

## Phase 5: User Story 3 - 准备质量门禁与增量提交流程 (Priority: P3)

**Goal**: 骨架包含测试、扫描、构建、部署、冒烟验证、README 记录和 GitHub 提交流程占位。

**Independent Test**: 执行 quickstart 中的测试和脚本占位，确认 README 有记录模板，质量门禁知道哪些项目已验证、哪些项目后续补齐。

### Tests for User Story 3

- [X] T065 [P] [US3] Create quickstart validation script test in `harness/backend/src/test/java/com/openclaw/harness/reports/QuickstartValidationTest.java`
- [X] T066 [P] [US3] Create report generation unit test in `harness/backend/src/test/java/com/openclaw/harness/reports/ExecutionReportServiceTest.java`
- [X] T067 [P] [US3] Create README development log check script in `infra/scripts/check-readme-log.ps1`

### Implementation for User Story 3

- [X] T068 [P] [US3] Create execution report model in `harness/backend/src/main/java/com/openclaw/harness/reports/ExecutionReport.java`
- [X] T069 [US3] Create execution report service in `harness/backend/src/main/java/com/openclaw/harness/reports/ExecutionReportService.java`
- [X] T070 [US3] Create report controller in `harness/backend/src/main/java/com/openclaw/harness/api/ReportController.java`
- [X] T071 [US3] Create Maven profile for verification in `harness/backend/pom.xml`
- [X] T072 [US3] Create Docker build instructions in `infra/docker/README.md`
- [X] T073 [US3] Create smoke validation script in `infra/scripts/harness-smoke.ps1`
- [X] T074 [US3] Create README log validation script in `infra/scripts/check-readme-log.ps1`
- [X] T075 [US3] Update quickstart implementation notes in `specs/001-init-agent-harness/quickstart.md`
- [X] T076 [US3] Update root README development log for US3 completion in `README.md`
- [X] T077 [US3] Commit US3 changes to GitHub with message `[harness] 新增：质量门禁与验证流程`

**Checkpoint**: US3 可独立演示：测试、报告、Docker、冒烟验证和 README/GitHub 记录流程均有可执行入口或明确占位。

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: 收敛文档、契约、测试、格式、扫描和发布前质量检查。

- [X] T078 [P] Validate REST contract alignment between `specs/001-init-agent-harness/contracts/openapi.yaml` and `harness/backend/src/main/java/com/openclaw/harness/api/`
- [X] T079 [P] Validate CLI contract alignment between `specs/001-init-agent-harness/contracts/cli-contract.md` and `harness/backend/docs/cli.md`
- [X] T080 Run backend unit and integration tests with `mvn test` from `harness/backend/pom.xml`
- [X] T081 Run backend verification profile with `mvn verify` from `harness/backend/pom.xml`
- [X] T082 [P] Run README development log validation using `infra/scripts/check-readme-log.ps1`
- [X] T083 [P] Run quickstart validation checklist against `specs/001-init-agent-harness/quickstart.md`
- [X] T084 [P] Review all generated documentation for Chinese-first compliance in `README.md`, `harness/README.md`, and `harness/backend/docs/`
- [X] T085 [P] Confirm no real secrets or server addresses exist in `harness/backend/src/main/resources/application.yml`
- [X] T086 Update root README final development log for the full Harness skeleton feature in `README.md`
- [X] T087 Commit final polish changes to GitHub with message `[harness] 完成：工程骨架初始化`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 Setup**: 无依赖，可立即开始。
- **Phase 2 Foundational**: 依赖 Phase 1，阻塞所有用户故事。
- **Phase 3 US1**: 依赖 Phase 2，是 MVP 范围。
- **Phase 4 US2**: 依赖 Phase 2，可在 US1 后实施；若人力充足，可与 US1 部分并行，但必须复用基础模型。
- **Phase 5 US3**: 依赖 Phase 2，建议在 US1 和 US2 后实施，以便质量门禁覆盖真实入口。
- **Phase 6 Polish**: 依赖计划内用户故事完成。

### User Story Dependencies

- **US1**: MVP；无其他用户故事依赖。
- **US2**: 依赖 Phase 2；文档上可独立验证，但体验上受益于 US1 的模块入口。
- **US3**: 依赖 Phase 2；质量门禁最终应覆盖 US1 和 US2 产物。

### Within Each User Story

- 测试任务优先于实现任务。
- 模型和 DTO 先于服务。
- 服务先于 Controller。
- 文档和 README 记录在每个故事验收前完成。
- 每个用户故事完成后必须独立提交 GitHub。

### Parallel Opportunities

- T004-T010 可在 T001-T003 后并行。
- T012-T018、T022-T024 可并行。
- US1 的 T025-T027 可并行，T028-T029 可并行。
- US2 的测试任务 T037-T042 可并行，模型/DTO 任务 T043-T045、T049、T052-T056、T059 可并行。
- US3 的 T065-T067 可并行，T068、T072、T073、T074 可并行。
- Phase 6 的 T078、T079、T082、T083、T084、T085 可并行。

---

## Parallel Example: User Story 1

```bash
Task: "T025 [P] [US1] Create health endpoint integration test in harness/backend/src/test/java/com/openclaw/harness/api/HealthControllerTest.java"
Task: "T026 [P] [US1] Create module registry unit test in harness/backend/src/test/java/com/openclaw/harness/agents/AgentModuleRegistryTest.java"
Task: "T027 [P] [US1] Create documentation boundary check script in infra/scripts/check-harness-docs.ps1"
Task: "T028 [P] [US1] Create agent module descriptor model in harness/backend/src/main/java/com/openclaw/harness/agents/AgentModuleDescriptor.java"
Task: "T029 [P] [US1] Create agent module status enum in harness/backend/src/main/java/com/openclaw/harness/agents/AgentModuleStatus.java"
```

## Parallel Example: User Story 2

```bash
Task: "T037 [P] [US2] Create task service unit test in harness/backend/src/test/java/com/openclaw/harness/state/HarnessTaskServiceTest.java"
Task: "T040 [P] [US2] Create human gate service unit test in harness/backend/src/test/java/com/openclaw/harness/gates/HumanGateServiceTest.java"
Task: "T043 [P] [US2] Create harness task model in harness/backend/src/main/java/com/openclaw/harness/state/HarnessTask.java"
Task: "T054 [P] [US2] Create human gate model in harness/backend/src/main/java/com/openclaw/harness/gates/HumanGate.java"
Task: "T059 [P] [US2] Create tool plugin descriptor in harness/backend/src/main/java/com/openclaw/harness/tools/ToolPluginDescriptor.java"
```

## Parallel Example: User Story 3

```bash
Task: "T065 [P] [US3] Create quickstart validation script test in harness/backend/src/test/java/com/openclaw/harness/reports/QuickstartValidationTest.java"
Task: "T066 [P] [US3] Create report generation unit test in harness/backend/src/test/java/com/openclaw/harness/reports/ExecutionReportServiceTest.java"
Task: "T067 [P] [US3] Create README development log check script in infra/scripts/check-readme-log.ps1"
Task: "T072 [US3] Create Docker build instructions in infra/docker/README.md"
Task: "T073 [US3] Create smoke validation script in infra/scripts/harness-smoke.ps1"
```

---

## Implementation Strategy

### MVP First (US1 Only)

1. 完成 Phase 1 和 Phase 2。
2. 完成 Phase 3 的 US1。
3. 验证健康检查、模块占位和中文文档入口。
4. 更新 README 并提交 GitHub。

### Incremental Delivery

1. Setup + Foundational：建立 Spring Boot 与公共模型基础。
2. US1：交付工程入口和模块边界。
3. US2：交付任务、日志、上下文、门禁、工具插件和产物约定。
4. US3：交付质量门禁、报告、冒烟验证和 README/GitHub 记录流程。
5. Polish：统一验证契约、测试、文档、安全和最终提交。

### Parallel Team Strategy

1. 一人负责 Maven/Spring Boot 基础和公共配置。
2. 一人负责 Agent 模块、任务状态和门禁模型。
3. 一人负责文档、quickstart、脚本和质量门禁。
4. 每个用户故事完成后合并并提交独立 GitHub commit。

---

## Notes

- 前端控制台本阶段只创建 `harness/console/README.md` 占位，不创建 Vue 3 或 React 应用。
- 第一阶段不接入真实云部署、不保存真实密钥、不执行线上破坏性操作。
- 本任务清单要求后续实现阶段按用户故事拆分提交；如果 GitHub 推送失败，必须在 `README.md` 记录阻塞原因。
- `tasks.md` 自身生成后也必须更新 `README.md` 并提交 GitHub。
