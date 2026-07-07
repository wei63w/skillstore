# Feature Specification: 真实模型驱动 Harness 端到端流水线

**Feature Branch**: `006-real-harness-pipeline`

**Created**: 2026-07-07

**Status**: Draft

**Input**: User description: "接入真实代码大模型 provider，提供配置文件占位进行配置；模型输出 JSON Schema 校验、补丁 diff 审阅、自动修复循环；把代码生成、测试、失败重试、Git 提交串成真正端到端流水线；支持生成/修改真实 skill-store 前后端业务代码；接入前端 E2E、SAST、依赖漏洞扫描、Docker 构建、部署执行器；更完整的权限沙箱和高危操作审批"

**Documentation Language**: 默认使用中文编写本文档。仅代码标识符、第三方协议、
API 字段、英文专有名词或用户明确要求的内容使用英文。

**Spec Kit Workflow Gate**: 本功能 MUST 按顺序执行 `specify` → `clarify` →
`checklist` → `plan` → `tasks` → `implement`。本规格由 `specify` 创建或更新后，
必须继续执行 `clarify` 并记录澄清结论，执行 `checklist` 并通过需求检查清单，然后
才能进入技术计划和任务拆解。

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 配置真实代码模型并生成可审阅补丁 (Priority: P1)

作为 Harness 操作者，我希望通过配置选择真实代码模型供应商，让系统基于当前 Spec Kit
产物生成可校验、可审阅、可拒绝的代码补丁，而不是只使用本地 stub 演示。

**Why this priority**: 真实代码模型接入是 Harness 从演示底座走向自主开发系统的关键前提。

**Independent Test**: 配置一个启用的代码模型供应商和一个禁用的供应商，提交同一功能生成请求；系统能够使用启用供应商返回符合结构要求的补丁，并对禁用或缺失配置给出明确阻断原因。

**Acceptance Scenarios**:

1. **Given** 已存在完整 feature 目录和有效模型配置，**When** 操作者发起代码生成，**Then** 系统返回结构化补丁、变更摘要、风险等级和审阅状态。
2. **Given** 模型返回内容缺少必需字段或格式不合法，**When** 系统校验模型输出，**Then** 本次补丁不得应用，并记录校验失败原因。
3. **Given** 模型配置缺少密钥或供应商被禁用，**When** 操作者发起生成，**Then** 系统阻断任务并给出可修复配置提示，且不得泄露密钥值。

---

### User Story 2 - 自动生成、测试、修复并提交真实 Skill Store 变更 (Priority: P2)

作为项目维护者，我希望 Harness 能把代码生成、补丁应用、后端测试、前端测试、失败重试和 Git 提交串成端到端流水线，使它能够生成或修改真实 `skill-store` 前后端代码，并保留完整证据。

**Why this priority**: 该能力直接验证交付物 A 能否向交付物 B 输出真实可运行代码，是项目主线里最核心的闭环。

**Independent Test**: 提供一个小型 Skill Store 功能需求，运行端到端流水线；系统能生成真实前后端改动、运行测试、在失败时自动修复，最终提交成功或在三轮失败后进入人工确认。

**Acceptance Scenarios**:

1. **Given** 一个明确的小功能需求和可运行的 Skill Store 工程，**When** Harness 执行端到端流水线，**Then** 系统生成或修改真实前后端代码、测试和文档，并提交到 GitHub。
2. **Given** 首轮生成代码导致测试失败，**When** 自动修复循环启动，**Then** 系统最多重试三轮，并把每轮失败原因、模型输入、模型输出和补丁差异持久化。
3. **Given** 三轮自动修复后仍无法通过关键测试，**When** 流水线达到失败阈值，**Then** 系统暂停并向人工输出结构化待确认事项。

---

### User Story 3 - 引入安全、构建、部署和审批门禁 (Priority: P3)

作为安全和运维负责人，我希望 Harness 在生成代码后自动执行安全扫描、依赖漏洞扫描、容器构建、部署执行和高危审批，确保任何发布候选都可审计、可回滚且不越权。

**Why this priority**: 自主开发系统只有具备安全左移、构建验证、部署门禁和权限沙箱，才能进入可信演示或线上环境。

**Independent Test**: 运行一个包含安全扫描、依赖扫描、容器构建和部署 dry-run 的流水线；系统能阻断高危问题，低风险场景自动推进，高危操作必须等待人工确认。

**Acceptance Scenarios**:

1. **Given** 生成代码包含高危安全问题，**When** 安全扫描执行，**Then** 系统阻断构建和部署，并生成漏洞等级、证据和修复建议。
2. **Given** 部署操作涉及端口开放、权限变更或线上环境写入，**When** 部署执行器准备执行，**Then** 系统必须进入人工确认门禁。
3. **Given** 构建或部署失败，**When** 系统具备上一稳定版本，**Then** 系统自动执行回滚或生成明确的回滚操作建议。

---

### User Story 4 - 沙箱权限和高危操作审批可观测 (Priority: P4)

作为平台管理员，我希望每个 Agent 操作都在权限沙箱内执行，且高危操作审批有明确风险等级、可选方案和审计记录。

**Why this priority**: 权限边界决定自主系统的安全上限，必须在真实模型和部署能力接入时同步加强。

**Independent Test**: 构造普通文件写入、高危路径写入、密钥读取、部署变更四类操作；系统能自动允许低风险操作、阻断越权操作、对高风险操作发起人工确认。

**Acceptance Scenarios**:

1. **Given** Agent 尝试访问非授权路径，**When** 沙箱策略评估操作，**Then** 系统拒绝执行并记录审计事件。
2. **Given** 操作需要访问密钥、权限、支付或部署资源，**When** 风险门禁触发，**Then** 系统输出待确认内容、风险等级、可选方案和默认建议。

### Edge Cases

- 模型供应商暂时不可用、超时、限流或返回空响应时，系统必须自动切换到可用供应商或进入可恢复失败状态。
- 模型输出补丁格式合法但语义上修改了非目标业务模块时，系统必须通过路径、任务关联和 diff 审阅阻断或要求人工确认。
- 自动修复循环生成相同失败补丁时，系统必须识别重复失败并提前终止无效重试。
- 前端 E2E 依赖浏览器、网络或测试数据时，系统必须区分环境故障和业务回归。
- 安全扫描存在无法自动修复的高危漏洞时，系统必须阻断后续构建和部署。
- Docker 构建成功但部署冒烟失败时，系统必须保留镜像、日志和回滚证据。
- README 记录或 GitHub 推送失败时，系统必须保留本地提交证据并记录阻塞原因。

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST allow operators to configure multiple real code model providers with enablement, priority, model name, timeout, retry, and secret reference placeholders.
- **FR-002**: System MUST never store plaintext model API keys, deployment credentials, database credentials, payment credentials, or administrator passwords in source files or generated reports.
- **FR-003**: System MUST validate every model output against a required machine-readable schema before any patch can be reviewed or applied.
- **FR-004**: System MUST persist model request metadata, response metadata, validation result, selected provider, redacted prompt summary, patch summary, and failure reason for every generation attempt.
- **FR-005**: Users MUST be able to review generated patch diffs before apply, including changed files, change type, risk level, source task, and model rationale.
- **FR-006**: System MUST support dry-run generation, approved apply, and rejected apply outcomes.
- **FR-007**: System MUST connect generation, schema validation, diff review, patch apply, backend tests, frontend tests, E2E tests, SAST, dependency scan, container build, deployment execution, Git commit, GitHub push, and report generation into one end-to-end pipeline.
- **FR-008**: System MUST automatically retry failed generation, test, scan, build, or deployment stages up to three rounds when the failure is classified as automatically repairable.
- **FR-009**: System MUST pause and request human confirmation when failures remain after retry limits, when requirements are ambiguous, or when high-risk permissions, credentials, payment, privacy, security, infrastructure, or deployment operations are detected.
- **FR-010**: System MUST generate and modify real OpenClaw Skill Store frontend, backend, test, documentation, and configuration files only inside approved project boundaries.
- **FR-011**: System MUST keep Agent Dev Harness core logic independent from Skill Store business rules; Skill Store-specific behavior may appear only in generated target artifacts, target adapters, or external configuration.
- **FR-012**: System MUST run backend unit and integration tests for generated backend changes and block the pipeline when required tests fail after automatic repair.
- **FR-013**: System MUST run frontend unit/build checks and E2E checks for generated frontend changes and block the pipeline when core user journeys fail after automatic repair.
- **FR-014**: System MUST run static security checks and dependency vulnerability checks before container build; high-risk findings must block build and deployment.
- **FR-015**: System MUST create container build artifacts and persist build logs, image identity, build inputs, and reproducibility evidence.
- **FR-016**: System MUST support deployment dry-run and controlled deployment execution with rollback evidence.
- **FR-017**: System MUST evaluate every tool, file, network, deployment, credential, and Git operation through a sandbox policy before execution.
- **FR-018**: System MUST classify operations by risk level and produce structured approval requests for high-risk operations.
- **FR-019**: System MUST update README development records after every independently verifiable small feature or repair with date, type, scope, verification result, and GitHub status.
- **FR-020**: System MUST create Git commits with project-standard messages and push to GitHub when all required gates pass or record the blocking reason when push is unavailable.
- **FR-021**: System MUST produce a final pipeline report that links specification, plan, tasks, generated diffs, tests, scans, build, deployment, approval, README, and GitHub evidence.
- **FR-022**: System MUST preserve enough state for the pipeline to resume from the last successful stage after process interruption.

### Constitution Requirements *(mandatory)*

- **CR-001**: Feature MUST persist replayable evidence for model selection, prompt construction, model response, schema validation, diff review, patch apply, tool calls, tests, scans, builds, deployments, approvals, Git commits, README updates, and final reports.
- **CR-002**: Feature MUST require human confirmation for ambiguous requirements, high-risk infrastructure changes, credential or permission changes, privacy/security-sensitive operations, deployment execution, destructive rollback, and failures unresolved after three automatic repair loops.
- **CR-003**: Feature MUST preserve Agent Dev Harness and OpenClaw Skill Store separation: Harness provides orchestration, policy, model, validation, test, build, deployment, and Git capabilities; Skill Store business code remains under the target application boundary.
- **CR-004**: Feature MUST specify authentication, authorization, input validation, file handling, secret handling, sandbox policy, model output validation, SAST, dependency scanning, and high-risk operation approval expectations.
- **CR-005**: Feature MUST cover unit tests, integration tests, frontend tests, E2E tests, SAST, dependency scan, build validation, deployment dry-run validation, and approval workflow tests.
- **CR-006**: Feature MUST keep Harness single-module generation target under 5 minutes where external provider latency permits, resume within 30 seconds, and support up to 3 independent pipelines concurrently.
- **CR-007**: Feature MUST output model provider configuration guide, pipeline operation guide, security policy guide, deployment guide, rollback guide, API contract, test report, security report, build report, deployment report, and final delivery report.
- **CR-008**: Feature MUST update `README.md` development records after each independently verifiable capability slice.
- **CR-009**: Feature MUST commit each independently verifiable slice with project-standard messages such as `[harness] 新增：真实模型供应商配置`, `[harness] 新增：模型输出校验与补丁审阅`, `[harness] 新增：端到端生成流水线`, `[harness] 新增：安全构建部署执行器`, and `[harness] 新增：权限沙箱与审批门禁`.
- **CR-010**: Feature MUST continue through `clarify`, `checklist`, `plan`, `tasks`, and `implement` before code, configuration, or deployment changes are applied.

### Key Entities *(include if feature involves data)*

- **Model Provider Configuration**: Represents a configurable code model supplier, including provider identity, enablement state, priority, model selection, timeout policy, retry policy, endpoint placeholder, and secret reference.
- **Generation Attempt**: Represents one model-driven attempt to create or repair a patch, including source feature, target tasks, provider used, validation status, diff summary, risks, and outcome.
- **Patch Review**: Represents an auditable review gate for generated changes, including diff files, risk level, schema validation, reviewer decision, and approval status.
- **Pipeline Run**: Represents the end-to-end autonomous delivery execution from generation through tests, scans, build, deployment, Git submission, and report generation.
- **Repair Loop**: Represents a bounded automatic retry cycle with failure classification, corrective context, generated patch, and retry count.
- **Sandbox Policy Decision**: Represents the allow, deny, or require-approval decision for a tool, file, network, credential, Git, build, or deployment operation.
- **Approval Request**: Represents a human confirmation task with risk level, proposed action, options, default recommendation, deadline, and decision record.
- **Execution Evidence**: Represents persisted logs, artifacts, reports, commit IDs, scan outputs, build identifiers, deployment results, and rollback evidence.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: At least two independently configured code model providers can be selected, disabled, or fail over without code changes.
- **SC-002**: 100% of model outputs are schema-validated before patch review or application.
- **SC-003**: 100% of generated patch applications include a persisted diff review record with changed files, risk level, and approval outcome.
- **SC-004**: A small Skill Store feature can be generated or modified end-to-end with tests, scans, build, README update, Git commit, GitHub push, and final report in one pipeline run.
- **SC-005**: Automatically repairable failures retry no more than three rounds, and 100% of unresolved failures produce a structured human confirmation request.
- **SC-006**: High-risk security findings, failed core E2E journeys, failed required tests, or unapproved high-risk operations block build or deployment 100% of the time.
- **SC-007**: Pipeline interruption can resume from the last successful stage within 30 seconds in local validation.
- **SC-008**: Up to 3 independent pipeline runs can be tracked concurrently without state collision.
- **SC-009**: 100% of agent, tool, model, test, scan, build, deployment, approval, Git, and report steps have persisted structured logs with trace IDs.
- **SC-010**: README development log includes the date, type, scope, verification result, and GitHub submission status for every committed capability slice.
- **SC-011**: Final report links all required Spec Kit artifacts and implementation evidence before the feature is marked complete.
- **SC-012**: No plaintext secrets, real payment credentials, database credentials, or administrator passwords appear in committed files or generated reports.

## Assumptions

- 第一版真实模型接入以代码/推理模型为主，多模态模型继续后置，不阻塞本功能验收。
- 模型密钥通过环境变量、外部配置中心或本地未提交配置注入；仓库只保留占位配置和说明。
- 端到端流水线优先支持本地和演示环境，真实云资源变更必须通过人工审批。
- Skill Store 是目标应用示例，Harness 只通过受控目标边界修改它，不把商城业务规则硬编码进 Harness 核心。
- 部署执行器第一阶段允许先支持 dry-run 和本地容器验证，真实远程部署在审批门禁和配置齐备后启用。
- 自动修复循环只处理可归因于生成补丁、测试失败、扫描失败、构建失败的可修复问题；权限、密钥、支付、隐私和高危基础设施问题默认需要人工确认。
