# Tasks: 真实模型驱动 Harness 端到端流水线

**Input**: Design documents from `specs/006-real-harness-pipeline/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Spec Kit Workflow Gate**: 当前 `specify`、requirements checklist、`plan` 已完成；`clarify` 未单独执行但规格无开放澄清，本任务清单将其作为显式回填记录纳入 Setup。`implement` 必须使用本文件为执行来源，随进度更新任务状态，并记录最终验证到 README。

**Documentation Language**: 默认中文；代码标识符和第三方协议保留英文。

**Tests**: 本功能强制包含单元、集成、REST 合约、SAST/依赖扫描、Docker dry-run、部署 dry-run 和 quickstart 验证任务。

## Phase 1: Setup

**Purpose**: 建立 006 实现目录、配置占位和流程证据。

- [x] T001 Create model config package files in `harness/backend/src/main/java/com/openclaw/harness/model/`
- [x] T002 Create pipeline package files in `harness/backend/src/main/java/com/openclaw/harness/pipeline/`
- [x] T003 Create sandbox package files in `harness/backend/src/main/java/com/openclaw/harness/sandbox/`
- [x] T004 Create security package files in `harness/backend/src/main/java/com/openclaw/harness/security/`
- [x] T005 Create deployment package files in `harness/backend/src/main/java/com/openclaw/harness/deployment/`
- [x] T006 [P] Add provider placeholder config in `harness/backend/src/main/resources/application-models.example.yml`
- [x] T007 [P] Add clarify backfill note in `specs/006-real-harness-pipeline/checklists/requirements.md`
- [x] T008 [P] Update provider guide in `harness/backend/docs/model-provider.md`

## Phase 2: Foundational

**Purpose**: 所有用户故事共享的 Schema、证据、命令和风险基础。

- [x] T009 [P] Add model provider config tests in `harness/backend/src/test/java/com/openclaw/harness/model/ModelProviderConfigTest.java`
- [x] T010 [P] Add model output schema validator tests in `harness/backend/src/test/java/com/openclaw/harness/model/ModelOutputSchemaValidatorTest.java`
- [x] T011 [P] Add patch review tests in `harness/backend/src/test/java/com/openclaw/harness/generation/PatchReviewServiceTest.java`
- [x] T012 [P] Add sandbox policy tests in `harness/backend/src/test/java/com/openclaw/harness/sandbox/SandboxPolicyServiceTest.java`
- [x] T013 Implement `ModelProviderConfig` in `harness/backend/src/main/java/com/openclaw/harness/model/ModelProviderConfig.java`
- [x] T014 Implement `ModelProviderSettings` in `harness/backend/src/main/java/com/openclaw/harness/model/ModelProviderSettings.java`
- [x] T015 Implement `ModelOutputSchemaValidator` in `harness/backend/src/main/java/com/openclaw/harness/model/ModelOutputSchemaValidator.java`
- [x] T016 Implement `PatchReview` records in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchReview.java`
- [x] T017 Implement `PatchReviewService` in `harness/backend/src/main/java/com/openclaw/harness/generation/PatchReviewService.java`
- [x] T018 Implement `SandboxOperation` and `SandboxDecision` in `harness/backend/src/main/java/com/openclaw/harness/sandbox/`
- [x] T019 Implement `SandboxPolicyService` in `harness/backend/src/main/java/com/openclaw/harness/sandbox/SandboxPolicyService.java`

## Phase 3: User Story 1 - 配置真实代码模型并生成可审阅补丁 (Priority: P1) 🎯 MVP

**Goal**: 支持真实 provider 配置占位、fake provider 本地验证、Schema 校验和 diff review。

**Independent Test**: 配置启用/禁用 provider 后生成补丁；合法输出进入 review，非法输出被拒绝。

- [x] T020 [P] [US1] Add provider registry selection tests in `harness/backend/src/test/java/com/openclaw/harness/model/ConfiguredCodeModelProviderRegistryTest.java`
- [x] T021 [P] [US1] Add fake real-provider tests in `harness/backend/src/test/java/com/openclaw/harness/model/HttpCodeModelProviderTest.java`
- [x] T022 [P] [US1] Add review REST contract tests in `harness/backend/src/test/java/com/openclaw/harness/api/PatchReviewControllerTest.java`
- [x] T023 [US1] Implement `ConfiguredCodeModelProviderRegistry` in `harness/backend/src/main/java/com/openclaw/harness/model/ConfiguredCodeModelProviderRegistry.java`
- [x] T024 [US1] Implement `HttpCodeModelProvider` with redacted config handling in `harness/backend/src/main/java/com/openclaw/harness/model/HttpCodeModelProvider.java`
- [x] T025 [US1] Extend `CodeGenerationExecutor` to validate schema before apply in `harness/backend/src/main/java/com/openclaw/harness/executors/CodeGenerationExecutor.java`
- [x] T026 [US1] Add `PatchReviewController` in `harness/backend/src/main/java/com/openclaw/harness/api/PatchReviewController.java`
- [x] T027 [US1] Update README development log and commit US1 with message `[harness] 新增：真实模型供应商配置`

## Phase 4: User Story 2 - 自动生成、测试、修复并提交真实 Skill Store 变更 (Priority: P2)

**Goal**: 串联生成、校验、审阅、应用、测试、修复、Git 提交为本地端到端流水线。

**Independent Test**: 发起 pipeline dry-run 后生成所有阶段结果和报告；可修复失败最多三轮。

- [x] T028 [P] [US2] Add pipeline model tests in `harness/backend/src/test/java/com/openclaw/harness/pipeline/PipelineRunTest.java`
- [x] T029 [P] [US2] Add pipeline service dry-run tests in `harness/backend/src/test/java/com/openclaw/harness/pipeline/PipelineServiceTest.java`
- [x] T030 [P] [US2] Add repair loop tests in `harness/backend/src/test/java/com/openclaw/harness/pipeline/RepairLoopServiceTest.java`
- [x] T031 [P] [US2] Add pipeline REST contract tests in `harness/backend/src/test/java/com/openclaw/harness/api/PipelineControllerTest.java`
- [x] T032 [US2] Implement pipeline records in `harness/backend/src/main/java/com/openclaw/harness/pipeline/`
- [x] T033 [US2] Implement `RepairLoopService` in `harness/backend/src/main/java/com/openclaw/harness/pipeline/RepairLoopService.java`
- [x] T034 [US2] Implement `PipelineService` dry-run orchestration in `harness/backend/src/main/java/com/openclaw/harness/pipeline/PipelineService.java`
- [x] T035 [US2] Add `PipelineController` in `harness/backend/src/main/java/com/openclaw/harness/api/PipelineController.java`
- [x] T036 [US2] Update README development log and commit US2 with message `[harness] 新增：端到端生成流水线`

## Phase 5: User Story 3 - 引入安全、构建、部署和审批门禁 (Priority: P3)

**Goal**: 安全扫描、依赖扫描、Docker 构建、部署 dry-run 和 rollback evidence 进入 pipeline。

**Independent Test**: 高危扫描阻断构建；部署操作生成审批或 dry-run 报告。

- [x] T037 [P] [US3] Add security scan planner tests in `harness/backend/src/test/java/com/openclaw/harness/security/SecurityScanPlannerTest.java`
- [x] T038 [P] [US3] Add docker build planner tests in `harness/backend/src/test/java/com/openclaw/harness/deployment/DockerBuildPlannerTest.java`
- [x] T039 [P] [US3] Add deployment dry-run tests in `harness/backend/src/test/java/com/openclaw/harness/deployment/DeploymentPlannerTest.java`
- [x] T040 [US3] Implement `SecurityScanPlanner` in `harness/backend/src/main/java/com/openclaw/harness/security/SecurityScanPlanner.java`
- [x] T041 [US3] Implement `DockerBuildPlanner` in `harness/backend/src/main/java/com/openclaw/harness/deployment/DockerBuildPlanner.java`
- [x] T042 [US3] Implement `DeploymentPlanner` and rollback record in `harness/backend/src/main/java/com/openclaw/harness/deployment/`
- [x] T043 [US3] Integrate security/build/deploy stages into `harness/backend/src/main/java/com/openclaw/harness/pipeline/PipelineService.java`
- [x] T044 [US3] Update README development log and commit US3 with message `[harness] 新增：安全构建部署执行器`

## Phase 6: User Story 4 - 沙箱权限和高危操作审批可观测 (Priority: P4)

**Goal**: 所有关键操作通过 sandbox 决策，高危操作生成审批请求。

**Independent Test**: 普通操作 allow，高危部署 require approval，越界路径 deny。

- [x] T045 [P] [US4] Add approval request tests in `harness/backend/src/test/java/com/openclaw/harness/sandbox/ApprovalRequestServiceTest.java`
- [x] T046 [P] [US4] Add sandbox REST contract tests in `harness/backend/src/test/java/com/openclaw/harness/api/SandboxControllerTest.java`
- [x] T047 [US4] Implement `ApprovalRequest` records in `harness/backend/src/main/java/com/openclaw/harness/sandbox/ApprovalRequest.java`
- [x] T048 [US4] Implement `ApprovalRequestService` in `harness/backend/src/main/java/com/openclaw/harness/sandbox/ApprovalRequestService.java`
- [x] T049 [US4] Add `SandboxController` in `harness/backend/src/main/java/com/openclaw/harness/api/SandboxController.java`
- [x] T050 [US4] Integrate approval decisions into `harness/backend/src/main/java/com/openclaw/harness/pipeline/PipelineService.java`
- [x] T051 [US4] Update README development log and commit US4 with message `[harness] 新增：权限沙箱与审批门禁`

## Phase 7: Polish

- [x] T052 [P] Update `harness/backend/docs/pipeline-operator-guide.md`
- [x] T053 [P] Update `harness/backend/docs/sandbox-policy.md`
- [x] T054 [P] Update `harness/backend/docs/deployment-runbook.md`
- [x] T055 [P] Run `mvn test` and update `harness/backend/docs/test-coverage.md`
- [x] T056 [P] Run `mvn verify` and update `harness/backend/docs/test-coverage.md`
- [x] T057 [P] Run `mvn dependency:tree -Dscope=runtime` and update `harness/backend/docs/security-scan.md`
- [x] T058 Validate `specs/006-real-harness-pipeline/quickstart.md`
- [x] T059 Generate final report in `harness/runtime/reports/006-real-harness-pipeline-report.md`
- [x] T060 Commit final validation with message `[harness] 完成：真实流水线验证报告`

## Dependencies & Execution Order

- Phase 1 -> Phase 2 -> US1 -> US2 -> US3 -> US4 -> Polish.
- MVP scope: US1，证明真实模型配置占位、Schema 校验和 diff review。
- US2 在 MVP 后串联本地 dry-run pipeline。
- US3/US4 增强安全、部署和审批。

## Parallel Opportunities

- T006/T007/T008 可并行。
- T009/T010/T011/T012 可并行。
- 每个用户故事中的 `[P]` 测试任务可并行。
- 文档更新 T052/T053/T054 可并行。

## Implementation Strategy

1. 先完成 Setup/Foundation，确保配置、Schema、Review 和 Sandbox 可测试。
2. 完成 US1 后停止验证并提交。
3. 完成 US2 dry-run pipeline 后提交。
4. 完成 US3/US4 后补齐安全部署和审批。
5. 最终运行 `mvn test`、`mvn verify`、依赖树检查、quickstart 验证，并生成最终报告。
