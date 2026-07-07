# Feature Specification: 完善 Harness CodeGenerationExecutor

**Feature Branch**: `master`

**Created**: 2026-07-07

**Status**: Draft

**Input**: User description: "完善 Harness CodeGenerationExecutor：根据 spec/plan/tasks/contracts 自动生成或修改 skill-store 前后端代码、测试和文档"

**Documentation Language**: 默认使用中文编写本文档。仅代码标识符、第三方协议、API 字段、英文专有名词或用户明确要求的内容使用英文。

**Spec Kit Workflow Gate**: 本功能 MUST 按顺序执行 `specify` → `clarify` → `checklist` → `plan` → `tasks` → `implement`。本规格由 `specify` 创建；后续必须继续执行 `clarify`、`checklist`、`plan`、`tasks` 和 `implement`。

## Clarifications

### Session 2026-07-07

- 自动生成可运行代码必须接入代码/推理型大模型能力，并且 MUST 支持多家代码大模型供应商；多模态模型不作为本阶段核心依赖，仅预留后续视觉评审或设计稿理解扩展点。
- 第一版模型调用必须通过统一抽象层完成，禁止业务执行器直接绑定单一供应商 SDK；密钥通过环境变量或配置中心注入，禁止写入仓库。
- 多模态能力后置为可选 `VisualModelProvider` 类扩展，不阻塞本功能的代码生成、测试失败分析、自动修复和报告摘要能力。

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 从规格生成受控代码补丁 (Priority: P1)

作为 Harness 使用者，我需要 CodeGenerationExecutor 读取目标功能的 `spec.md`、`plan.md`、`tasks.md` 和 `contracts/`，生成可审查、可应用的代码补丁，以便 Harness 能把文档化需求转化为 OpenClaw Skill 商店前后端真实文件变更。

**Why this priority**: 当前 CodeGenerationExecutor 只能生成受控 Markdown 产物，不能真正推动交付物 B 自动生成；代码补丁能力是从交付物 A 生成交付物 B 的关键断点。

**Independent Test**: 给定一个受控 Skill 商店功能规格和任务清单，执行器能输出补丁计划、目标文件列表、变更摘要和可应用的文件变更，且不修改 Harness 核心业务边界。

**Acceptance Scenarios**:

1. **Given** 一个完整的 Skill 商店功能目录，**When** Harness 调用 CodeGenerationExecutor，**Then** 系统生成针对 `skill-store/backend`、`skill-store/frontend`、`docs` 或 `infra` 的受控补丁计划。
2. **Given** 任务清单中包含后端、前端、测试和文档任务，**When** 执行器生成变更，**Then** 每个变更都能追溯到任务 ID、规格段落或契约条目。
3. **Given** 生成内容涉及高风险配置或敏感信息，**When** 执行器准备应用补丁，**Then** 系统阻塞并生成结构化人工确认请求。

---

### User Story 2 - 应用补丁并保持文件边界安全 (Priority: P2)

作为项目维护者，我需要 Harness 只允许 CodeGenerationExecutor 修改允许范围内的业务应用文件，并拒绝越界、密钥、生产配置和破坏性文件操作，以便自动生成交付物 B 时不破坏 Harness 或本机环境。

**Why this priority**: 自动写代码如果缺少边界控制，会直接冲突于安全左移、Harness/业务解耦和 Agent 沙箱原则。

**Independent Test**: 给定合法和非法补丁，执行器只应用合法补丁；越界路径、`.env`、密钥、生产支付配置和删除类操作必须被阻断并记录原因。

**Acceptance Scenarios**:

1. **Given** 补丁目标位于 `skill-store/` 允许目录内，**When** 风险扫描通过，**Then** 系统应用补丁并记录变更文件。
2. **Given** 补丁目标试图修改 Harness 核心、`.env` 或仓库外路径，**When** 执行器校验路径，**Then** 系统拒绝应用并记录高风险原因。
3. **Given** 补丁包含真实密钥、真实支付配置或生产数据库地址，**When** 执行器扫描内容，**Then** 系统阻塞并请求人工确认。

---

### User Story 3 - 自动生成测试和文档变更 (Priority: P3)

作为验收人员，我需要 CodeGenerationExecutor 在生成业务代码时同步生成或更新测试和中文文档，以便自动生成的 Skill 商店功能具备可验证、可交付和可维护证据。

**Why this priority**: 只生成业务代码不足以满足项目宪章；交付物 B 必须有测试、文档和报告闭环。

**Independent Test**: 给定一个业务功能任务，执行器生成的补丁必须包含对应后端/前端测试或明确不适用说明，并更新相关中文文档或 README 开发记录候选内容。

**Acceptance Scenarios**:

1. **Given** 任务要求新增后端接口，**When** 生成补丁，**Then** 补丁包含服务/接口测试或契约测试。
2. **Given** 任务要求新增前端流程，**When** 生成补丁，**Then** 补丁包含组件/路由/交互测试或 E2E 占位。
3. **Given** 代码变更生成成功，**When** 执行器输出结果，**Then** 系统同步生成中文文档摘要和 README 开发记录候选内容。

---

### User Story 4 - 将生成结果接入测试、重试和报告闭环 (Priority: P4)

作为项目负责人，我需要 CodeGenerationExecutor 的生成结果自动进入 TestExecutor、RetryOrchestrator、DeliveryReport 和 GitSubmitExecutor，以便 Harness 能完成“生成代码 → 测试 → 修复 → 报告 → 提交”的闭环。

**Why this priority**: 自动生成交付物 B 必须能被验证和提交，否则只是一次性代码输出，不是长运行自主开发系统。

**Independent Test**: 对一个小型 Skill 商店演示任务运行流水线后，系统能应用补丁、运行测试、记录失败重试、生成报告，并在验证通过后准备 README 和 Git 提交证据。

**Acceptance Scenarios**:

1. **Given** 补丁已成功应用，**When** Harness 进入测试阶段，**Then** 系统按任务定义运行后端、前端、构建和安全检查。
2. **Given** 生成代码导致测试失败，**When** 失败可自动修复，**Then** 系统最多执行三轮修复并记录每轮变更。
3. **Given** 生成和验证均通过，**When** 流水线收尾，**Then** 报告包含生成输入、补丁摘要、测试结果、重试记录、风险扫描和 Git 状态。

### Edge Cases

- 当 `spec.md`、`plan.md`、`tasks.md` 或 `contracts/` 缺失时，执行器必须拒绝生成并提示缺失产物。
- 当任务描述无法映射到明确文件变更时，执行器必须生成人工确认请求，而不是猜测写入任意文件。
- 当目标路径超出允许目录、包含路径穿越或试图修改 Harness 核心时，执行器必须阻断。
- 当生成内容包含密钥、真实支付、生产数据库、管理员凭据或高风险部署配置时，执行器必须阻断。
- 当补丁部分应用失败时，执行器必须回滚已应用变更或保留可审计的失败状态。
- 当测试三轮自动修复仍失败时，执行器必须停止并请求人工确认。

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: CodeGenerationExecutor MUST 读取目标功能目录中的 `spec.md`、`plan.md`、`tasks.md`、`contracts/` 和必要设计产物。
- **FR-002**: CodeGenerationExecutor MUST 根据任务 ID、用户故事、契约和目标模块生成可审查的补丁计划。
- **FR-003**: CodeGenerationExecutor MUST 支持为 `skill-store/backend` 生成或修改后端业务代码、接口、服务、数据访问、测试和文档。
- **FR-004**: CodeGenerationExecutor MUST 支持为 `skill-store/frontend` 生成或修改页面、路由、状态、接口封装、测试和文档。
- **FR-005**: CodeGenerationExecutor MUST 支持更新 `docs/`、`skill-store/README.md` 或根 README 开发记录候选内容。
- **FR-006**: CodeGenerationExecutor MUST 将每个生成文件映射到任务 ID、规格来源、契约来源和变更原因。
- **FR-007**: CodeGenerationExecutor MUST 在应用补丁前校验允许路径，禁止仓库外路径、路径穿越、Harness 核心越界修改、`.env`、密钥和生产配置。
- **FR-008**: CodeGenerationExecutor MUST 对生成内容进行敏感信息和高风险配置扫描。
- **FR-009**: CodeGenerationExecutor MUST 在风险扫描失败、需求无法映射、越界修改或高风险配置出现时触发人工确认门禁。
- **FR-010**: CodeGenerationExecutor MUST 支持 dry-run 模式，输出补丁计划和变更摘要但不写入文件。
- **FR-011**: CodeGenerationExecutor MUST 支持 apply 模式，在风险扫描通过后写入文件并记录变更清单。
- **FR-012**: CodeGenerationExecutor MUST 将生成结果交给 TestExecutor 运行相关测试、构建和安全检查。
- **FR-013**: CodeGenerationExecutor MUST 与 RetryOrchestrator 协作，对可修复失败最多三轮自动修复。
- **FR-014**: CodeGenerationExecutor MUST 将补丁计划、应用结果、测试结果、重试记录和剩余风险写入 DeliveryReport。
- **FR-015**: CodeGenerationExecutor MUST 保持 Harness 核心与 OpenClaw Skill 商店业务规则分离，不得把商城业务规则硬编码进 Harness 通用执行器。

### Constitution Requirements *(mandatory)*

- **CR-001**: 本功能 MUST 将代码生成输入、补丁计划、文件变更、工具调用、测试输出、重试和报告记录为持久可回放证据。
- **CR-002**: 本功能 MUST 明确需求歧义、越界修改、凭据、支付、隐私、安全和三轮失败后的人工确认门禁。
- **CR-003**: 本功能 MUST 保持 Agent Dev Harness 与 OpenClaw Skill 商店业务代码分离；Harness 负责生成和编排，不内嵌商城业务规则。
- **CR-004**: 本功能 MUST 指定输入校验、路径校验、文件处理、密钥处理、安全扫描和依赖扫描要求。
- **CR-005**: 本功能 MUST 指定单元测试、集成测试、受控补丁应用测试、前端测试占位、安全扫描和依赖扫描要求。
- **CR-006**: 本功能 MUST 声明代码生成任务目标耗时不超过 5 分钟，失败恢复目标不超过 30 秒。
- **CR-007**: 本功能 MUST 输出补丁计划、生成报告、测试报告、风险扫描结果、更新后的 API/CLI 契约和中文操作说明。
- **CR-008**: 本功能 MUST 更新 README.md 开发记录，说明本次规格创建和后续实现追踪方式。
- **CR-009**: 本功能 MUST 以独立 GitHub 提交完成规格创建，提交信息遵循 `[模块] 动作：内容`。
- **CR-010**: 本功能 MUST 严格执行 `specify`、`clarify`、`checklist`、`plan`、`tasks`、`implement` 顺序。

### Key Entities *(include if feature involves data)*

- **生成请求**: 表示一次基于功能目录和任务范围的代码生成请求。
- **规格上下文包**: 表示从 `spec.md`、`plan.md`、`tasks.md`、`contracts/` 和设计产物抽取出的生成上下文。
- **补丁计划**: 表示将要创建、修改或拒绝的文件列表、任务映射、风险等级和生成原因。
- **文件变更项**: 表示单个文件的目标路径、变更类型、内容摘要、来源任务和风险扫描结果。
- **补丁应用结果**: 表示 dry-run 或 apply 的结果、成功文件、失败文件、回滚状态和错误原因。
- **生成风险记录**: 表示越界路径、敏感信息、高风险配置、需求歧义或安全风险。
- **生成报告**: 表示代码生成阶段的输入、输出、测试、重试、风险和交付证据汇总。

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 给定完整 Skill 商店功能目录，执行器能在 5 分钟内生成补丁计划和变更摘要。
- **SC-002**: 100% 的生成文件能追溯到任务 ID、规格来源或契约来源。
- **SC-003**: 100% 的越界路径、`.env`、密钥、真实支付和生产配置变更在应用前被阻断。
- **SC-004**: 对受控演示功能，执行器能生成后端、前端、测试和文档中至少三类变更。
- **SC-005**: 生成后的验证结果、失败重试和风险扫描 100% 写入最终报告。
- **SC-006**: 代码生成失败后能在 30 秒内恢复到最近成功阶段或明确阻塞状态。
- **SC-007**: 验证通过的小功能 100% 更新 README 开发记录并形成 GitHub 提交或明确阻塞原因。
- **SC-008**: Harness 核心代码中不出现 OpenClaw Skill 商店具体业务流程硬编码。

## Assumptions

- 本功能优先完善交付物 A 的代码生成执行能力，用于后续自动生成或重建交付物 B。
- 第一版默认生成受控补丁计划和文件变更，不直接接入真实外部大模型 API 或云部署 API。
- 目标业务应用默认位于 `skill-store/`，允许修改范围由 Harness 配置或计划产物声明。
- 生成器可以基于模板、现有代码模式、规格上下文和任务清单生成文件；复杂歧义进入人工确认。
- 真实支付、真实密钥、生产数据库、线上部署和权限变更默认不自动执行。
