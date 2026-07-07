# Tasks: 完善 Agent Dev Harness 自主开发能力

**Input**: Design documents from `/specs/004-harness-automation/`

**Prerequisites**: [plan.md](./plan.md), [spec.md](./spec.md), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/](./contracts/), [quickstart.md](./quickstart.md)

**Spec Kit Workflow Gate**: 本任务清单在 `specify`、`clarify`、`checklist`、`plan` 已按顺序完成后生成。后续 `implement` 必须以本文为执行来源，随实现推进更新任务状态，并在 README 记录最终验证和 GitHub 提交状态。

**Documentation Language**: 默认使用中文编写任务、文档和报告；代码标识符、API 字段、第三方协议名保留英文。

**Tests**: 本功能规格明确要求测试、构建、安全/依赖扫描、恢复和重试验证，因此每个 User Story 均包含测试任务，且测试应先于实现编写。

## Format: `[ID] [P?] [Story] Description`

- **[P]**: 可并行执行，前提是不同文件且不依赖未完成任务
- **[Story]**: 仅 User Story 阶段使用，映射 `US1`、`US2`、`US3`、`US4`
- 每条任务均包含明确文件路径

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: 为自主开发闭环创建包结构、配置和文档入口。

- [x] T001 Create workflow, executors, retry package directories under `harness/backend/src/main/java/com/openclaw/harness/`
- [x] T002 Create matching test package directories under `harness/backend/src/test/java/com/openclaw/harness/`
- [x] T003 [P] Add workflow runtime configuration properties in `harness/backend/src/main/java/com/openclaw/harness/config/WorkflowRuntimeProperties.java`
- [x] T004 [P] Add command execution documentation stub in `harness/backend/docs/automation-workflow.md`
- [x] T005 [P] Add automation quick validation script placeholder in `infra/scripts/harness-automation-smoke.ps1`
- [x] T006 Update Harness module overview for automation scope in `harness/README.md`
- [x] T007 Confirm Spec Kit workflow evidence for `specify`, `clarify`, `checklist`, `plan`, and `tasks` in `specs/004-harness-automation/tasks.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: 建立所有故事共享的数据模型、持久化、命令执行和风险判断基础。

**CRITICAL**: 本阶段完成前不得开始任何 User Story 实现。

- [x] T008 Create SpecKitStage enum with strict stage order in `harness/backend/src/main/java/com/openclaw/harness/workflow/SpecKitStage.java`
- [x] T009 Create WorkflowStatus enum in `harness/backend/src/main/java/com/openclaw/harness/workflow/WorkflowStatus.java`
- [x] T010 [P] Create PhaseStatus enum in `harness/backend/src/main/java/com/openclaw/harness/workflow/PhaseStatus.java`
- [x] T011 [P] Create DevelopmentTask record in `harness/backend/src/main/java/com/openclaw/harness/workflow/DevelopmentTask.java`
- [x] T012 [P] Create PhaseExecution record in `harness/backend/src/main/java/com/openclaw/harness/workflow/PhaseExecution.java`
- [x] T013 [P] Create ToolInvocation record in `harness/backend/src/main/java/com/openclaw/harness/executors/ToolInvocation.java`
- [x] T014 [P] Create RetryAttempt record in `harness/backend/src/main/java/com/openclaw/harness/retry/RetryAttempt.java`
- [x] T015 [P] Create GitDeliveryRecord record in `harness/backend/src/main/java/com/openclaw/harness/executors/GitDeliveryRecord.java`
- [x] T016 Implement JSON file persistence helper for runtime artifacts in `harness/backend/src/main/java/com/openclaw/harness/workflow/WorkflowStateStore.java`
- [x] T017 Implement command result model in `harness/backend/src/main/java/com/openclaw/harness/executors/CommandResult.java`
- [x] T018 Implement safe command executor with working-directory validation in `harness/backend/src/main/java/com/openclaw/harness/executors/CommandExecutor.java`
- [x] T019 Implement risk classifier for commands and file changes in `harness/backend/src/main/java/com/openclaw/harness/gates/RiskClassifier.java`
- [x] T020 Add foundational unit tests for stage ordering and persistence in `harness/backend/src/test/java/com/openclaw/harness/workflow/WorkflowFoundationTest.java`
- [x] T021 Add foundational unit tests for command execution and risk classification in `harness/backend/src/test/java/com/openclaw/harness/executors/CommandExecutorTest.java`

**Checkpoint**: 基础模型、持久化、命令执行和风险分类可用后，User Story 可按优先级实施。

---

## Phase 3: User Story 1 - 自动编排 Spec Kit 开发流水线 (Priority: P1) MVP

**Goal**: Harness 接收业务目标后，按顺序执行或恢复 `specify → clarify → checklist → plan → tasks → implement`，并在产物缺失或风险门禁时阻塞。

**Independent Test**: 使用受控目标启动 workflow，验证阶段顺序、产物检查、失败阻断和断点恢复均可独立通过。

### Tests for User Story 1

- [x] T022 [P] [US1] Add unit tests for strict Spec Kit stage transitions in `harness/backend/src/test/java/com/openclaw/harness/workflow/SpecKitWorkflowServiceTest.java`
- [x] T023 [P] [US1] Add recovery tests for checkpoint resume within 30 seconds in `harness/backend/src/test/java/com/openclaw/harness/workflow/WorkflowResumeServiceTest.java`
- [x] T024 [P] [US1] Add REST contract tests for workflow start, resume, and phases endpoints in `harness/backend/src/test/java/com/openclaw/harness/api/WorkflowControllerTest.java`
- [x] T025 [P] [US1] Add CLI contract tests for workflow start and resume outputs in `harness/backend/src/test/java/com/openclaw/harness/cli/WorkflowCliContractTest.java`

### Implementation for User Story 1

- [x] T026 [US1] Implement Spec Kit artifact validator in `harness/backend/src/main/java/com/openclaw/harness/workflow/SpecKitArtifactValidator.java`
- [x] T027 [US1] Implement workflow orchestration service in `harness/backend/src/main/java/com/openclaw/harness/workflow/SpecKitWorkflowService.java`
- [x] T028 [US1] Implement workflow resume service in `harness/backend/src/main/java/com/openclaw/harness/workflow/WorkflowResumeService.java`
- [x] T029 [US1] Implement phase execution persistence integration in `harness/backend/src/main/java/com/openclaw/harness/workflow/PhaseExecutionRecorder.java`
- [x] T030 [US1] Create REST DTOs for workflow operations in `harness/backend/src/main/java/com/openclaw/harness/api/dto/WorkflowRequests.java`
- [x] T031 [US1] Create REST DTOs for workflow responses in `harness/backend/src/main/java/com/openclaw/harness/api/dto/WorkflowResponses.java`
- [x] T032 [US1] Implement workflow REST controller in `harness/backend/src/main/java/com/openclaw/harness/api/WorkflowController.java`
- [x] T033 [US1] Add workflow command documentation to `harness/backend/docs/cli.md`
- [x] T034 [US1] Update README development log for US1 implementation in `README.md`
- [x] T035 [US1] Commit US1 implementation to GitHub with message `[harness] 新增：Spec Kit 流水线编排器`

**Checkpoint**: US1 完成后，Harness 能独立启动、记录、阻断和恢复 Spec Kit 流水线。

---

## Phase 4: User Story 2 - 编排代码生成、测试和自动修复 (Priority: P2)

**Goal**: Harness 基于 `tasks.md` 执行代码生成、测试、构建、安全/依赖检查，并对可修复失败最多重试三轮。

**Independent Test**: 给定含未完成任务的测试 feature，Harness 能解析任务、执行受控命令、记录失败、重试三轮并在耗尽后触发人工确认。

### Tests for User Story 2

- [x] T036 [P] [US2] Add parser tests for tasks.md checkboxes, dependencies, and parallel markers in `harness/backend/src/test/java/com/openclaw/harness/workflow/TaskChecklistParserTest.java`
- [x] T037 [P] [US2] Add code generation executor tests using controlled templates in `harness/backend/src/test/java/com/openclaw/harness/executors/CodeGenerationExecutorTest.java`
- [x] T038 [P] [US2] Add test executor tests for unit, integration, build, security, and dependency profiles in `harness/backend/src/test/java/com/openclaw/harness/executors/TestExecutorTest.java`
- [x] T039 [P] [US2] Add retry policy tests for three-round limit and human gate escalation in `harness/backend/src/test/java/com/openclaw/harness/retry/RetryPolicyTest.java`
- [x] T040 [P] [US2] Add REST contract tests for run-tasks and tests endpoints in `harness/backend/src/test/java/com/openclaw/harness/api/WorkflowExecutionControllerTest.java`

### Implementation for User Story 2

- [x] T041 [US2] Implement task checklist item model in `harness/backend/src/main/java/com/openclaw/harness/workflow/TaskChecklistItem.java`
- [x] T042 [US2] Implement tasks.md parser in `harness/backend/src/main/java/com/openclaw/harness/workflow/TaskChecklistParser.java`
- [x] T043 [US2] Implement task dependency planner in `harness/backend/src/main/java/com/openclaw/harness/workflow/TaskExecutionPlanner.java`
- [x] T044 [US2] Implement controlled code generation executor in `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java`
- [x] T045 [US2] Implement test execution profile enum in `harness/backend/src/main/java/com/openclaw/harness/executors/TestProfile.java`
- [x] T046 [US2] Implement test executor command mapping in `harness/backend/src/main/java/com/openclaw/harness/executors/TestExecutor.java`
- [x] T047 [US2] Implement retry policy and failure classifier in `harness/backend/src/main/java/com/openclaw/harness/retry/RetryPolicy.java`
- [x] T048 [US2] Integrate retry exhaustion with human gates in `harness/backend/src/main/java/com/openclaw/harness/retry/RetryOrchestrator.java`
- [x] T049 [US2] Implement workflow execution REST controller in `harness/backend/src/main/java/com/openclaw/harness/api/WorkflowExecutionController.java`
- [x] T050 [US2] Update automation workflow documentation for codegen, tests, and retry in `harness/backend/docs/automation-workflow.md`
- [x] T051 [US2] Update README development log for US2 implementation in `README.md`
- [x] T052 [US2] Commit US2 implementation to GitHub with message `[harness] 新增：任务执行与失败重试闭环`

**Checkpoint**: US2 完成后，Harness 能从 `tasks.md` 驱动实现、测试和三轮修复闭环。

---

## Phase 5: User Story 3 - 持久化日志、产物和报告 (Priority: P3)

**Goal**: Harness 为阶段、工具调用、代码差异、测试日志、构建记录、失败原因和最终报告建立可审计证据链。

**Independent Test**: 执行完整或失败流水线后，可通过任务 ID 找到阶段日志、工具调用输出、产物索引和最终报告。

### Tests for User Story 3

- [x] T053 [P] [US3] Add tool invocation persistence tests in `harness/backend/src/test/java/com/openclaw/harness/observability/ToolInvocationRecorderTest.java`
- [x] T054 [P] [US3] Add artifact index tests in `harness/backend/src/test/java/com/openclaw/harness/reports/ArtifactIndexServiceTest.java`
- [x] T055 [P] [US3] Add delivery report generation tests in `harness/backend/src/test/java/com/openclaw/harness/reports/DeliveryReportServiceTest.java`
- [x] T056 [P] [US3] Add report REST contract tests in `harness/backend/src/test/java/com/openclaw/harness/api/WorkflowReportControllerTest.java`

### Implementation for User Story 3

- [x] T057 [US3] Implement tool invocation recorder in `harness/backend/src/main/java/com/openclaw/harness/observability/ToolInvocationRecorder.java`
- [x] T058 [US3] Implement code diff summary service in `harness/backend/src/main/java/com/openclaw/harness/observability/CodeDiffSummaryService.java`
- [x] T059 [US3] Implement artifact index service in `harness/backend/src/main/java/com/openclaw/harness/reports/ArtifactIndexService.java`
- [x] T060 [US3] Extend execution report model for phase, retry, gate, test, scan, and Git summaries in `harness/backend/src/main/java/com/openclaw/harness/reports/DeliveryReport.java`
- [x] T061 [US3] Implement delivery report service in `harness/backend/src/main/java/com/openclaw/harness/reports/DeliveryReportService.java`
- [x] T062 [US3] Implement workflow report REST controller in `harness/backend/src/main/java/com/openclaw/harness/api/WorkflowReportController.java`
- [x] T063 [US3] Document runtime artifact layout in `harness/backend/docs/runtime-artifacts.md`
- [x] T064 [US3] Update README development log for US3 implementation in `README.md`
- [x] T065 [US3] Commit US3 implementation to GitHub with message `[harness] 新增：流水线证据与报告生成`

**Checkpoint**: US3 完成后，任一流水线都有可回放日志、产物索引和最终报告。

---

## Phase 6: User Story 4 - 自动提交 GitHub 并形成交付证据 (Priority: P4)

**Goal**: Harness 在验证通过后检查提交风险、更新 README、生成规范提交、创建本地提交并推送 GitHub；失败时保留阻塞证据。

**Independent Test**: 在受控临时 Git 仓库中完成小功能后，验证 README 记录、提交信息、提交哈希、推送状态和阻塞原因均可查询。

### Tests for User Story 4

- [x] T066 [P] [US4] Add README development log updater tests in `harness/backend/src/test/java/com/openclaw/harness/executors/ReadmeDevelopmentLogUpdaterTest.java`
- [x] T067 [P] [US4] Add secret and high-risk config scan tests in `harness/backend/src/test/java/com/openclaw/harness/executors/PreCommitRiskScannerTest.java`
- [x] T068 [P] [US4] Add Git submit executor tests with a temporary repository in `harness/backend/src/test/java/com/openclaw/harness/executors/GitSubmitExecutorTest.java`
- [x] T069 [P] [US4] Add REST contract tests for git-submit endpoint in `harness/backend/src/test/java/com/openclaw/harness/api/GitSubmitControllerTest.java`

### Implementation for User Story 4

- [x] T070 [US4] Implement README development log updater in `harness/backend/src/main/java/com/openclaw/harness/executors/ReadmeDevelopmentLogUpdater.java`
- [x] T071 [US4] Implement pre-commit risk scanner in `harness/backend/src/main/java/com/openclaw/harness/executors/PreCommitRiskScanner.java`
- [x] T072 [US4] Implement commit message validator in `harness/backend/src/main/java/com/openclaw/harness/executors/CommitMessageValidator.java`
- [x] T073 [US4] Implement Git submit executor in `harness/backend/src/main/java/com/openclaw/harness/executors/GitSubmitExecutor.java`
- [x] T074 [US4] Implement Git submit REST controller in `harness/backend/src/main/java/com/openclaw/harness/api/GitSubmitController.java`
- [x] T075 [US4] Update CLI contract documentation for git submit behavior in `specs/004-harness-automation/contracts/cli-contract.md`
- [x] T076 [US4] Update README development log for US4 implementation in `README.md`
- [x] T077 [US4] Commit US4 implementation to GitHub with message `[harness] 新增：README 与 GitHub 交付证据`

**Checkpoint**: US4 完成后，Harness 能把验证通过的小功能转化为 README 和 GitHub 证据。

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: 验证全链路质量门禁、文档和交付证据。

- [ ] T078 [P] Run `mvn test` and record results in `harness/backend/docs/test-coverage.md`
- [ ] T079 [P] Run `mvn verify` and record build results in `harness/backend/docs/test-coverage.md`
- [ ] T080 [P] Run dependency and security scan commands and update `harness/backend/docs/security-scan.md`
- [ ] T081 [P] Run quickstart validation and update `specs/004-harness-automation/quickstart.md`
- [ ] T082 Validate REST contract coverage against `specs/004-harness-automation/contracts/openapi.yaml`
- [ ] T083 Validate CLI contract coverage against `specs/004-harness-automation/contracts/cli-contract.md`
- [ ] T084 Confirm docs are Chinese-first in `harness/backend/docs/automation-workflow.md`
- [ ] T085 Confirm no OpenClaw Skill Store business rules were added to `harness/backend/src/main/java/com/openclaw/harness/`
- [ ] T086 Confirm README contains records for all completed small features in `README.md`
- [ ] T087 Generate final implementation report in `harness/runtime/reports/004-harness-automation-report.md`
- [ ] T088 Commit final validation to GitHub with message `[harness] 完成：自主开发能力验证报告`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: 无依赖，可立即执行。
- **Foundational (Phase 2)**: 依赖 Phase 1，阻塞所有 User Story。
- **US1 (Phase 3)**: 依赖 Phase 2，是 MVP。
- **US2 (Phase 4)**: 依赖 Phase 2，可在 US1 接口稳定后集成；实现阶段建议在 US1 后执行。
- **US3 (Phase 5)**: 依赖 Phase 2，可与 US2 部分并行，但最终报告需集成 US1/US2 结果。
- **US4 (Phase 6)**: 依赖 Phase 2 和至少一个可验证功能完成；建议在 US1 后先实现基础提交能力，再随后续故事复用。
- **Polish (Phase 7)**: 依赖目标 User Story 完成。

### User Story Dependencies

- **US1 (P1)**: MVP，可独立交付，证明 Spec Kit 严格流程。
- **US2 (P2)**: 可独立测试任务解析、执行和重试；最终与 US1 编排集成。
- **US3 (P3)**: 可独立测试日志和报告；最终汇总 US1/US2/US4 证据。
- **US4 (P4)**: 可独立使用临时 Git 仓库测试；最终接入每个小功能收尾流程。

### Parallel Opportunities

- T003、T004、T005 可并行。
- T010 到 T015 可并行创建模型。
- 每个 User Story 的测试任务可并行编写。
- US2 的执行器测试和 US3 的报告测试可在基础模型完成后并行。
- US4 的 README、风险扫描、Git 提交测试可并行。

---

## Parallel Example: User Story 1

```bash
Task: "T022 Add unit tests for strict Spec Kit stage transitions in harness/backend/src/test/java/com/openclaw/harness/workflow/SpecKitWorkflowServiceTest.java"
Task: "T023 Add recovery tests for checkpoint resume within 30 seconds in harness/backend/src/test/java/com/openclaw/harness/workflow/WorkflowResumeServiceTest.java"
Task: "T024 Add REST contract tests for workflow start, resume, and phases endpoints in harness/backend/src/test/java/com/openclaw/harness/api/WorkflowControllerTest.java"
Task: "T025 Add CLI contract tests for workflow start and resume outputs in harness/backend/src/test/java/com/openclaw/harness/cli/WorkflowCliContractTest.java"
```

## Parallel Example: User Story 2

```bash
Task: "T036 Add parser tests for tasks.md checkboxes, dependencies, and parallel markers in harness/backend/src/test/java/com/openclaw/harness/workflow/TaskChecklistParserTest.java"
Task: "T037 Add code generation executor tests using controlled templates in harness/backend/src/test/java/com/openclaw/harness/executors/CodeGenerationExecutorTest.java"
Task: "T038 Add test executor tests for unit, integration, build, security, and dependency profiles in harness/backend/src/test/java/com/openclaw/harness/executors/TestExecutorTest.java"
Task: "T039 Add retry policy tests for three-round limit and human gate escalation in harness/backend/src/test/java/com/openclaw/harness/retry/RetryPolicyTest.java"
```

## Parallel Example: User Story 3

```bash
Task: "T053 Add tool invocation persistence tests in harness/backend/src/test/java/com/openclaw/harness/observability/ToolInvocationRecorderTest.java"
Task: "T054 Add artifact index tests in harness/backend/src/test/java/com/openclaw/harness/reports/ArtifactIndexServiceTest.java"
Task: "T055 Add delivery report generation tests in harness/backend/src/test/java/com/openclaw/harness/reports/DeliveryReportServiceTest.java"
Task: "T056 Add report REST contract tests in harness/backend/src/test/java/com/openclaw/harness/api/WorkflowReportControllerTest.java"
```

## Parallel Example: User Story 4

```bash
Task: "T066 Add README development log updater tests in harness/backend/src/test/java/com/openclaw/harness/executors/ReadmeDevelopmentLogUpdaterTest.java"
Task: "T067 Add secret and high-risk config scan tests in harness/backend/src/test/java/com/openclaw/harness/executors/PreCommitRiskScannerTest.java"
Task: "T068 Add Git submit executor tests with a temporary repository in harness/backend/src/test/java/com/openclaw/harness/executors/GitSubmitExecutorTest.java"
Task: "T069 Add REST contract tests for git-submit endpoint in harness/backend/src/test/java/com/openclaw/harness/api/GitSubmitControllerTest.java"
```

---

## Implementation Strategy

### MVP First (US1 Only)

1. Complete Phase 1: Setup。
2. Complete Phase 2: Foundational。
3. Complete Phase 3: US1。
4. Run US1 tests and quick workflow smoke validation。
5. Update README and commit US1 to GitHub。

### Incremental Delivery

1. US1: Spec Kit 流程编排和恢复。
2. US2: `tasks.md` 驱动的实现、测试和三轮重试。
3. US3: 持久化日志、产物索引和最终报告。
4. US4: README 与 GitHub 交付证据自动化。
5. Phase 7: 全链路验证和报告。

### Quality Gates

- 任一阶段缺少必需产物时阻断后续阶段。
- 测试、构建、安全或依赖扫描失败时最多自动修复三轮。
- 三轮失败、真实密钥、支付/隐私/权限、端口开放、线上部署或用户未提交变更风险必须触发人工确认。
- 覆盖率低于 80%、高危漏洞、核心流程失败或 README/GitHub 记录缺失时不得标记功能完成。
