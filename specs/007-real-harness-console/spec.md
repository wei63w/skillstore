# Feature Specification: 真实模型执行与 Harness Console

**Feature Branch**: `007-real-harness-console`

**Created**: 2026-07-07

**Status**: Draft

**Input**: User description: "完善真实模型调用：DeepSeek/OpenAI/Claude/Codex 真实流水线执行 真实 SAST/漏洞扫描 做一个轻量 Vue 3 Harness Console：左侧任务列表，中间 pipeline 阶段，右侧日志/审批/diff 面板，docker compose 部署前后端"

**Documentation Language**: 默认使用中文编写本文档。仅代码标识符、第三方协议、
API 字段、英文专有名词或用户明确要求的内容使用英文。

**Spec Kit Workflow Gate**: 本功能 MUST 按顺序执行 `specify` → `clarify` →
`checklist` → `plan` → `tasks` → `implement`。本规格由 `specify` 创建或更新后，
必须继续执行 `clarify` 并记录澄清结论，执行 `checklist` 并通过需求检查清单，然后
才能进入技术计划和任务拆解。

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 使用真实代码模型生成补丁 (Priority: P1)

作为 Harness 操作者，我希望在本地配置 DeepSeek、OpenAI、Claude 或 Codex 供应商后，系统能真实调用所选模型，返回符合结构要求的补丁计划，并在失败时给出可追踪错误。

**Why this priority**: 真实模型调用是从 dry-run 演示走向可用自主开发系统的第一关。

**Independent Test**: 设置一个真实 provider 的环境变量，提交同一 feature 目录，系统真实调用模型并返回 Schema 校验通过或失败的结构化结果；未配置 key 时明确阻断且不泄露密钥。

**Acceptance Scenarios**:

1. **Given** 已配置可用模型密钥和完整 Spec Kit feature，**When** 操作者选择 DeepSeek/OpenAI/Claude/Codex 发起生成，**Then** 系统真实调用供应商并返回结构化补丁计划。
2. **Given** 供应商超时、限流或返回非法结构，**When** 生成失败，**Then** 系统记录 provider、错误类型、重试次数和可恢复建议。
3. **Given** 未配置密钥或 provider 被禁用，**When** 操作者请求真实模型，**Then** 系统拒绝调用并提示缺失环境变量名，不输出密钥内容。

---

### User Story 2 - 执行真实流水线与安全扫描 (Priority: P2)

作为项目维护者，我希望 Harness 能真实执行代码生成、补丁审阅、补丁应用、后端测试、前端测试、E2E、SAST、依赖漏洞扫描、Docker 构建和 Git 提交流水线，而不是只记录 dry-run。

**Why this priority**: 只有真实执行器跑通，Harness 才能验证“自动打造交付物 B”的核心目标。

**Independent Test**: 选择一个小型 Skill Store 变更，运行真实流水线；系统应用补丁、运行测试和扫描，成功时生成提交候选，失败时记录阻断阶段并触发自动修复或审批。

**Acceptance Scenarios**:

1. **Given** 目标变更通过 diff review，**When** 操作者批准 apply，**Then** 系统真实修改允许目录内的 Skill Store 文件并保留 diff 证据。
2. **Given** 后端、前端或 E2E 测试失败，**When** 失败可自动修复，**Then** 系统最多执行三轮模型修复并记录每轮证据。
3. **Given** SAST 或依赖扫描发现高危问题，**When** 流水线进入构建阶段前，**Then** 系统阻断 Docker 构建和部署，并生成安全报告。

---

### User Story 3 - 使用轻量 Harness Console 操作流水线 (Priority: P3)

作为操作者，我希望打开 Vue 3 Harness Console 后，能在左侧查看任务列表，在中间查看 pipeline 阶段，在右侧查看日志、审批和 diff 面板，从而不必只靠命令行操作。

**Why this priority**: 控制台让长运行任务可观察、可审阅、可审批，是人机协作最小干预原则的交互入口。

**Independent Test**: 启动前后端后访问控制台；页面显示任务列表、阶段状态、日志、审批请求和 diff 摘要；用户可以发起 dry-run/真实 pipeline 并处理审批。

**Acceptance Scenarios**:

1. **Given** 后端已有 pipeline run，**When** 用户打开控制台，**Then** 左侧任务列表显示 run id、状态、当前阶段和更新时间。
2. **Given** 某个 run 有阶段结果，**When** 用户选中任务，**Then** 中间区域显示阶段时间线和每阶段状态。
3. **Given** 某个 run 有日志、审批或 diff，**When** 用户切换右侧面板，**Then** 可以查看详情并对待审批项做出批准或拒绝。

---

### User Story 4 - 使用 Docker Compose 本地部署前后端 (Priority: P4)

作为本地验证者，我希望通过一个 Docker Compose 命令启动 Harness 后端和 Vue 控制台，并保留环境变量占位、日志目录和运行数据挂载。

**Why this priority**: Compose 部署让本地演示更可复现，也为后续云部署打基础。

**Independent Test**: 在无明文密钥的本地环境执行 compose 启动；后端健康检查通过，前端可访问，运行目录持久化，未配置真实模型 key 时仍能使用 stub/fake 验证。

**Acceptance Scenarios**:

1. **Given** 本地安装 Docker，**When** 用户执行 compose 启动，**Then** 后端健康检查和前端首页均可访问。
2. **Given** 用户设置模型环境变量，**When** compose 启动后发起真实模型请求，**Then** 后端通过环境变量读取 key，仓库文件不包含明文 key。
3. **Given** 容器重启，**When** 用户再次访问控制台，**Then** runtime 报告和任务证据仍可读取。

### Edge Cases

- 真实模型返回自然语言而非结构化补丁时，系统必须拒绝应用并进入修复或人工审阅。
- 供应商接口短暂不可用时，系统必须按配置重试，并在超限后切换可用 provider 或阻断。
- 控制台刷新或后端重启时，不能丢失已持久化的 run 状态和审批记录。
- SAST 或依赖扫描工具本地不可用时，系统必须将该阶段标记为环境阻断，而不是误判为业务成功。
- Docker Compose 缺少模型密钥时，真实 provider 不可用，但 stub/fake 验证应继续可用。
- 高危部署、端口、权限、凭据、支付或隐私操作必须等待审批，不能由控制台默认自动批准。

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST support configurable real model providers for DeepSeek, OpenAI, Claude, and Codex using environment variable secret references only.
- **FR-002**: System MUST call the selected real provider when enabled and configured, and MUST record request metadata, provider, latency, retry count, response status, schema validation result, and redacted error details.
- **FR-003**: System MUST validate all model outputs against the required patch schema before diff review or application.
- **FR-004**: System MUST support provider fallback or structured failure when a provider is disabled, missing credentials, timed out, rate limited, or returns invalid output.
- **FR-005**: System MUST execute approved patch application against allowed Skill Store paths and block unauthorized paths.
- **FR-006**: System MUST execute backend tests, frontend tests, E2E tests, SAST, dependency vulnerability scan, Docker build, Git commit, and report generation as real pipeline stages when configured tools are available.
- **FR-007**: System MUST distinguish environment/tooling failures from code/test/security failures.
- **FR-008**: System MUST block build/deployment on high-risk SAST findings, high-risk dependency findings, failed required tests, unapproved high-risk operations, or schema-invalid model output.
- **FR-009**: System MUST retry automatically repairable failures up to three rounds and then open a human approval or intervention request.
- **FR-010**: Harness Console MUST provide a three-region working interface: left task/run list, middle pipeline stage timeline, right log/approval/diff panel.
- **FR-011**: Harness Console MUST allow users to start a pipeline, inspect stage evidence, review diffs, and approve or reject pending gates.
- **FR-012**: Harness Console MUST show clear loading, empty, failed, blocked, waiting approval, running, and succeeded states.
- **FR-013**: System MUST expose backend APIs needed by the console for run list, run detail, logs, diff review, approval decision, model provider status, and pipeline start.
- **FR-014**: Docker Compose MUST start backend and frontend services with configurable ports, runtime volume persistence, and environment variable based secrets.
- **FR-015**: Docker Compose MUST not require real model keys for basic local demo; stub/fake mode must remain usable.
- **FR-016**: System MUST persist all model, pipeline, test, scan, build, approval, Git, and deployment evidence under controlled runtime directories.
- **FR-017**: README and feature docs MUST explain local run, compose run, model key setup, safety gates, and known limitations.
- **FR-018**: Every independently verifiable slice MUST update README development records and be committed to GitHub.

### Constitution Requirements *(mandatory)*

- **CR-001**: Feature MUST persist replayable evidence for real model calls, schema validation, diff review, pipeline stages, test/scanner output, Docker build, approval decisions, Git submission, console actions, and deployment attempts.
- **CR-002**: Feature MUST require human confirmation for high-risk deployment, permissions, credentials, privacy/security-sensitive operations, and failures unresolved after three automatic repairs.
- **CR-003**: Feature MUST preserve Harness and Skill Store separation; the console operates Harness APIs and does not embed Skill Store business rules.
- **CR-004**: Feature MUST specify authentication/authorization expectations for console actions, input validation, file boundaries, secret handling, model output validation, SAST, dependency scanning, and upload/file handling if introduced.
- **CR-005**: Feature MUST cover backend unit/integration tests, frontend component/build tests, E2E tests for the console primary flow, SAST/dependency scan validation, Docker Compose validation, and smoke checks.
- **CR-006**: Feature MUST preserve Harness goals: code generation target within 5 minutes where provider latency permits, resume within 30 seconds, and up to 3 concurrent pipelines tracked without state collision.
- **CR-007**: Feature MUST produce model provider setup docs, console user guide, API contracts, test report, security scan report, Docker Compose runbook, and final delivery report.
- **CR-008**: Feature MUST update `README.md` for every independently verifiable capability slice.
- **CR-009**: Feature MUST commit each slice with messages such as `[harness] 新增：真实模型调用`, `[harness] 新增：真实流水线执行`, `[harness-console] 新增：控制台界面`, `[harness] 新增：Compose部署`.
- **CR-010**: Feature MUST continue through `clarify`, `checklist`, `plan`, `tasks`, and `implement` before implementation starts.

### Key Entities *(include if feature involves data)*

- **Real Model Provider**: Represents DeepSeek/OpenAI/Claude/Codex configuration, enabled status, secret reference, endpoint, model name, timeout, retry policy, and health status.
- **Model Invocation**: Represents one real call attempt, including provider, redacted request metadata, response metadata, schema validation, latency, retry count, and error details.
- **Pipeline Run**: Represents one end-to-end execution with current status, selected provider, target feature, target workspace, stage results, approvals, and report path.
- **Pipeline Stage Evidence**: Represents logs and artifacts for generation, tests, E2E, SAST, dependency scan, Docker build, deployment, Git, and report.
- **Console Task Item**: Represents the run list item shown in the Harness Console.
- **Approval Decision**: Represents user approval/rejection from the console with risk level, reason, decision maker, timestamp, and affected stage.
- **Compose Deployment Profile**: Represents local deployment settings, service ports, volumes, environment variables, health checks, and persistence rules.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: At least one real provider among DeepSeek/OpenAI/Claude/Codex can be enabled via environment variable and successfully return a schema-validated result in local validation.
- **SC-002**: 100% of model responses are schema-validated before diff review or apply.
- **SC-003**: A configured real pipeline can execute generation, apply, backend test, frontend test/build, SAST/dependency scan, Docker build, Git/report stages or produce a precise environment blocker for unavailable tools.
- **SC-004**: High-risk scan findings, schema-invalid output, failed required tests, and unapproved high-risk operations block build/deployment 100% of the time.
- **SC-005**: Console first screen loads within 2 seconds locally and shows task list, pipeline stage area, and right-side log/approval/diff panel.
- **SC-006**: Users can start a pipeline and inspect stage evidence from the console in under 5 clicks.
- **SC-007**: Users can approve or reject a pending high-risk gate from the console, and the decision is persisted with timestamp and reason.
- **SC-008**: Docker Compose starts backend and frontend locally with one command, and both health/smoke checks pass.
- **SC-009**: Runtime evidence remains available after container restart in local compose validation.
- **SC-010**: No plaintext model API keys or deployment credentials are committed or rendered in reports/UI.
- **SC-011**: Backend and frontend automated tests pass, and final report links spec, plan, tasks, tests, scan/build/deploy evidence, README records, and GitHub commits.

## Assumptions

- Vue 3 is the default console framework because the user explicitly requested a lightweight Vue 3 Harness Console.
- Real remote deployment beyond local Docker Compose remains gated by human approval.
- The first real provider enabled for local validation may be DeepSeek because a `DEEPSEEK_API_KEY` environment variable convention already exists.
- If Codex is not exposed as a direct HTTP model provider in the local environment, it will be represented as a provider adapter contract until credentials and endpoint are available.
- Console authentication can start as local trusted development mode, but all high-risk actions still require explicit approval records.
- Existing Harness backend remains the source of truth; the console is an operator UI over Harness APIs, not a separate orchestration brain.
