# Tasks: 完善 Harness CodeGenerationExecutor

**Input**: Design documents from `/specs/005-code-generation-executor/`

**Prerequisites**: [plan.md](./plan.md), [spec.md](./spec.md), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/](./contracts/), [quickstart.md](./quickstart.md)

**Spec Kit Workflow Gate**: 本任务清单在 `specify`、`clarify`、`checklist`、`plan` 已按顺序完成后生成。`implement` 必须以本文为执行来源，完成任务后更新 checkbox，并在 README 记录验证和 GitHub 状态。

## Phase 1: Setup

- [ ] T001 Create model and generation package directories in `harness/backend/src/main/java/com/openclaw/harness/`
- [ ] T002 Create matching test directories in `harness/backend/src/test/java/com/openclaw/harness/`
- [ ] T003 [P] Document model provider policy in `harness/backend/docs/model-provider.md`
- [ ] T004 [P] Add code generation runtime notes in `harness/backend/docs/automation-workflow.md`

## Phase 2: Foundation

- [ ] T005 Create CodeModelProvider interface in `harness/backend/src/main/java/com/openclaw/harness/model/CodeModelProvider.java`
- [ ] T006 [P] Create CodeModelRequest record in `harness/backend/src/main/java/com/openclaw/harness/model/CodeModelRequest.java`
- [ ] T007 [P] Create CodeModelResponse record in `harness/backend/src/main/java/com/openclaw/harness/model/CodeModelResponse.java`
- [ ] T008 Create CodeModelProviderRegistry in `harness/backend/src/main/java/com/openclaw/harness/model/CodeModelProviderRegistry.java`
- [ ] T009 Create StubCodeModelProvider in `harness/backend/src/main/java/com/openclaw/harness/model/StubCodeModelProvider.java`
- [ ] T010 Create GenerationMode enum in `harness/backend/src/main/java/com/openclaw/harness/generation/GenerationMode.java`
- [ ] T011 [P] Create CodeGenerationRequest record in `harness/backend/src/main/java/com/openclaw/harness/generation/CodeGenerationRequest.java`
- [ ] T012 [P] Create GenerationContext record in `harness/backend/src/main/java/com/openclaw/harness/generation/GenerationContext.java`

## Phase 3: User Story 1 - 从规格生成受控代码补丁 (Priority: P1)

**Goal**: 根据 spec/plan/tasks/contracts 生成可审查补丁计划。

- [ ] T013 [P] [US1] Add provider registry tests in `harness/backend/src/test/java/com/openclaw/harness/model/CodeModelProviderRegistryTest.java`
- [ ] T014 [P] [US1] Add generation context loader tests in `harness/backend/src/test/java/com/openclaw/harness/generation/GenerationContextLoaderTest.java`
- [ ] T015 [P] [US1] Add patch plan tests in `harness/backend/src/test/java/com/openclaw/harness/generation/PatchPlanTest.java`
- [ ] T016 [US1] Create FileChange record in `harness/backend/src/main/java/com/openclaw/harness/generation/FileChange.java`
- [ ] T017 [US1] Create PatchPlan record in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchPlan.java`
- [ ] T018 [US1] Implement GenerationContextLoader in `harness/backend/src/main/java/com/openclaw/harness/generation/GenerationContextLoader.java`
- [ ] T019 [US1] Update CodeGenerationExecutor to request PatchPlan from provider in `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java`
- [ ] T020 [US1] Update README development log and commit US1 with message `[harness] 新增：多模型补丁计划生成`

## Phase 4: User Story 2 - 应用补丁并保持文件边界安全 (Priority: P2)

**Goal**: 支持 dry-run/apply，阻断越界路径和敏感内容。

- [ ] T021 [P] [US2] Add path policy tests in `harness/backend/src/test/java/com/openclaw/harness/generation/PatchPathPolicyTest.java`
- [ ] T022 [P] [US2] Add risk scanner tests in `harness/backend/src/test/java/com/openclaw/harness/generation/GenerationRiskScannerTest.java`
- [ ] T023 [P] [US2] Add patch applier tests in `harness/backend/src/test/java/com/openclaw/harness/generation/PatchApplierTest.java`
- [ ] T024 [US2] Implement PatchPathPolicy in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchPathPolicy.java`
- [ ] T025 [US2] Implement GenerationRiskScanner in `harness/backend/src/main/java/com/openclaw/harness/generation/GenerationRiskScanner.java`
- [ ] T026 [US2] Create PatchApplyResult record in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchApplyResult.java`
- [ ] T027 [US2] Implement PatchApplier in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchApplier.java`
- [ ] T028 [US2] Integrate dry-run/apply into CodeGenerationExecutor in `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java`
- [ ] T029 [US2] Update README development log and commit US2 with message `[harness] 新增：代码补丁安全应用`

## Phase 5: User Story 3 - 自动生成测试和文档变更 (Priority: P3)

**Goal**: 生成结果包含测试或文档变更候选。

- [ ] T030 [P] [US3] Add stub provider tests for docs and tests changes in `harness/backend/src/test/java/com/openclaw/harness/model/StubCodeModelProviderTest.java`
- [ ] T031 [US3] Extend StubCodeModelProvider with backend/frontend/docs/test file changes in `harness/backend/src/main/java/com/openclaw/harness/model/StubCodeModelProvider.java`
- [ ] T032 [US3] Update code generation contract with documentation/test behavior in `specs/005-code-generation-executor/contracts/code-generation-contract.md`
- [ ] T033 [US3] Update README development log and commit US3 with message `[harness] 新增：测试与文档生成候选`

## Phase 6: User Story 4 - 接入测试、重试和报告闭环 (Priority: P4)

**Goal**: 提供 REST 入口和报告证据。

- [ ] T034 [P] [US4] Add REST contract tests in `harness/backend/src/test/java/com/openclaw/harness/api/CodeGenerationControllerTest.java`
- [ ] T035 [P] [US4] Add report persistence tests in `harness/backend/src/test/java/com/openclaw/harness/generation/CodeGenerationReportTest.java`
- [ ] T036 [US4] Implement CodeGenerationController in `harness/backend/src/main/java/com/openclaw/harness/api/CodeGenerationController.java`
- [ ] T037 [US4] Persist code generation report in `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java`
- [ ] T038 [US4] Update README development log and commit US4 with message `[harness] 新增：代码生成闭环入口`

## Phase 7: Polish

- [ ] T039 [P] Run `mvn test` and update `harness/backend/docs/test-coverage.md`
- [ ] T040 [P] Run `mvn verify` and update `harness/backend/docs/test-coverage.md`
- [ ] T041 [P] Run dependency check command and update `harness/backend/docs/security-scan.md`
- [ ] T042 Validate quickstart and update `specs/005-code-generation-executor/quickstart.md`
- [ ] T043 Generate final report in `harness/runtime/reports/005-code-generation-executor-report.md`
- [ ] T044 Commit final validation with message `[harness] 完成：代码生成执行器验证报告`

## Dependencies & Execution Order

- Phase 1 -> Phase 2 -> US1 -> US2 -> US3 -> US4 -> Polish.
- MVP scope: US1 + US2，证明多模型补丁计划和安全应用。
- US3/US4 在 MVP 后增强生成质量和闭环证据。

## Parallel Opportunities

- T003/T004 可并行。
- T006/T007/T011/T012 可并行。
- 每个 User Story 的 `[P]` 测试任务可并行。
