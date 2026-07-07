# Feature Specification: 完善 Agent Dev Harness 自主开发能力

**Feature Branch**: `master`

**Created**: 2026-07-07

**Status**: Draft

**Input**: User description: "完善 Harness 的能力：任务编排、Spec Kit 流程执行器、代码生成执行器、测试执行器、日志持久化、失败重试、Git 提交、报告生成。"

**Documentation Language**: 默认使用中文编写本文档。仅代码标识符、第三方协议、API 字段、英文专有名词或用户明确要求的内容使用英文。

**Spec Kit Workflow Gate**: 本功能 MUST 按顺序执行 `specify` → `clarify` → `checklist` → `plan` → `tasks` → `implement`。本规格由 `specify` 创建；后续必须继续执行 `clarify`、`checklist`、`plan`、`tasks` 和 `implement`。

## Clarifications

### Session 2026-07-07

- No critical ambiguities detected worth formal clarification. 当前规格已明确本阶段优先完善交付物 A，OpenClaw Skill 商店作为后续由 A 自动生成或重建的目标业务案例；代码生成执行器先以可控模板、任务上下文和本地工具调用方式实现，真实云端破坏性操作、支付/隐私/权限/密钥等高风险动作均进入人工确认门禁。

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 自动编排 Spec Kit 开发流水线 (Priority: P1)

作为人类工程师，我需要 Agent Dev Harness 接收一个业务开发目标后，自动编排 `specify`、`clarify`、`checklist`、`plan`、`tasks` 和 `implement` 的完整流程，以便后续交付物 B 能由交付物 A 按标准流程从 0 到 1 生成。

**Why this priority**: 严格 Spec Kit 流程是项目宪章的前置门禁；Harness 如果不能自动执行这条链路，就无法证明它是自主开发系统。

**Independent Test**: 给 Harness 一个新功能目标后，系统能创建功能目录，按顺序推进每个阶段，生成或更新对应产物，并在任一步骤失败时保留可回放状态。

**Acceptance Scenarios**:

1. **Given** 工程师提交一个明确业务目标，**When** Harness 启动开发流水线，**Then** 系统按顺序执行规格、澄清、清单、计划、任务和实现阶段。
2. **Given** 某阶段发现需求歧义或风险门禁，**When** Harness 无法自动决策，**Then** 系统暂停并输出结构化人工确认问题。
3. **Given** 流水线中途被中断，**When** 工程师重新启动同一任务，**Then** 系统能从最近成功阶段继续，而不是从头重做。

---

### User Story 2 - 编排代码生成、测试和自动修复 (Priority: P2)

作为项目维护者，我需要 Harness 根据 `tasks.md` 逐项调用代码生成、测试执行和失败修复能力，以便实现结果和任务清单、规格、计划保持一致。

**Why this priority**: 只生成文档不是自主开发；Harness 必须能把任务转化为代码、测试、修复和验证闭环。

**Independent Test**: 给定一个包含可执行任务的功能目录，Harness 能按任务依赖执行代码生成和测试，失败后最多重试三轮，并记录每轮失败原因和修改结果。

**Acceptance Scenarios**:

1. **Given** `tasks.md` 中存在未完成任务，**When** Harness 执行实现阶段，**Then** 系统按任务顺序执行并更新任务状态。
2. **Given** 某个测试失败，**When** Harness 判断可自动修复，**Then** 系统生成修复尝试、重新运行测试并记录每轮结果。
3. **Given** 同一问题三轮自动修复仍失败，**When** Harness 无法继续，**Then** 系统阻塞并请求人工确认。

---

### User Story 3 - 持久化日志、产物和报告 (Priority: P3)

作为审计和运维人员，我需要 Harness 对每个阶段的输入、输出、工具调用、代码差异、测试日志、构建记录、失败原因和最终报告进行持久化，以便复盘、回放、恢复和验收。

**Why this priority**: 长周期智能体系统的可信度来自可观测、可复现和可审计证据。

**Independent Test**: 执行一次完整或失败的开发流水线后，审计人员能从任务 ID 找到阶段日志、工具调用记录、测试输出、代码变更摘要和最终报告。

**Acceptance Scenarios**:

1. **Given** Harness 执行任一阶段，**When** 阶段完成或失败，**Then** 系统记录阶段输入、输出、耗时、状态和错误信息。
2. **Given** Harness 生成或修改代码，**When** 阶段结束，**Then** 系统记录代码差异摘要和相关测试结果。
3. **Given** 流水线结束，**When** 工程师查看报告，**Then** 报告包含需求、计划、任务、实现、测试、扫描、Git 和遗留风险。

---

### User Story 4 - 自动提交 GitHub 并形成交付证据 (Priority: P4)

作为项目负责人，我需要 Harness 在每个可独立验证的小功能完成后自动更新 README 开发记录、创建规范提交并推送到 GitHub，以便项目演进具备可追踪交付证据。

**Why this priority**: 项目宪章要求每个小功能、修改或修复都必须更新 README 并提交到 GitHub。

**Independent Test**: 完成一个小功能后，Harness 能展示拟提交范围、提交信息、验证结果和 README 记录；低风险提交自动完成，高风险或失败提交进入人工确认。

**Acceptance Scenarios**:

1. **Given** 某功能所有验证通过，**When** Harness 收尾，**Then** 系统更新 README 开发记录并生成规范提交信息。
2. **Given** GitHub 推送失败，**When** Harness 无法完成远端提交，**Then** 系统记录阻塞原因并保留本地提交状态。
3. **Given** 提交范围包含高风险配置、密钥或线上部署变更，**When** Harness 准备提交，**Then** 系统阻塞并请求人工确认。

### Edge Cases

- 当业务目标描述模糊且会影响范围、权限、安全或部署时，Harness 必须暂停并请求澄清。
- 当 Spec Kit 某阶段产物缺失或检查清单未通过时，Harness 必须阻断实现阶段。
- 当测试、构建、安全扫描或依赖审计失败时，Harness 必须记录失败原因并按最多三轮自动修复策略处理。
- 当任务中断、进程退出或上下文丢失时，Harness 必须能从持久化状态恢复。
- 当 Git 工作区存在用户未提交变更时，Harness 必须识别并避免覆盖用户变更。
- 当功能涉及支付、隐私、上传、权限、密钥、端口开放或线上部署时，Harness 必须触发人工确认。
- 当交付物 B 的目标规格变化时，Harness 必须基于最新规格重新规划，而不是复用过期计划。

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Harness MUST 接收一个业务开发目标，并为其创建或定位唯一开发任务。
- **FR-002**: Harness MUST 自动执行并记录 `specify`、`clarify`、`checklist`、`plan`、`tasks`、`implement` 阶段顺序。
- **FR-003**: Harness MUST 在每个阶段开始和结束时持久化状态、输入、输出、耗时、工具调用和错误信息。
- **FR-004**: Harness MUST 支持中断后从最近成功阶段恢复执行。
- **FR-005**: Harness MUST 基于 `tasks.md` 的任务状态、依赖顺序和并行标记驱动实现阶段。
- **FR-006**: Harness MUST 提供代码生成执行器，用于根据任务和上下文生成、修改或补齐代码与文档。
- **FR-007**: Harness MUST 提供测试执行器，用于运行功能指定的单元测试、集成测试、前端测试、构建和安全/依赖检查。
- **FR-008**: Harness MUST 对可自动修复的失败执行最多三轮修复尝试，并记录每轮输入、输出和验证结果。
- **FR-009**: Harness MUST 在三轮修复仍失败、需求歧义或高风险操作时生成结构化人工确认请求。
- **FR-010**: Harness MUST 生成最终报告，包含阶段状态、产物路径、代码变更摘要、测试结果、扫描结果、失败重试记录、人工确认记录和剩余风险。
- **FR-011**: Harness MUST 在验证通过后更新 README 开发记录，包含日期、类型、范围、说明、验证结果和 GitHub 状态。
- **FR-012**: Harness MUST 支持生成规范 Git 提交信息、提交本地变更并推送到 GitHub。
- **FR-013**: Harness MUST 在提交前检查密钥、真实支付配置、高风险部署配置和用户未提交变更。
- **FR-014**: Harness MUST 能以 OpenClaw Skill 商店作为目标业务案例，使用自身能力从规格开始生成或重建交付物 B。
- **FR-015**: Harness MUST 保留所有关键产物索引，包括规格、澄清、检查清单、计划、任务、代码差异、测试日志、构建产物和报告。

### Constitution Requirements *(mandatory)*

- **CR-001**: 本功能 MUST 定义 Agent、工具、服务、测试、构建、Git 和部署相关动作如何记录为持久、可回放证据。
- **CR-002**: 本功能 MUST 明确需求歧义、高风险基础设施、权限、凭据、支付、隐私、安全和无法自动修复失败的人工确认门禁。
- **CR-003**: 本功能 MUST 保持 Agent Dev Harness 与 OpenClaw Skill 商店业务代码分离；Harness 只能编排生成业务应用，不得内嵌业务规则。
- **CR-004**: 本功能 MUST 指定认证、授权、输入校验、文件处理、密钥处理、安全扫描和依赖扫描在 Harness 执行链路中的要求。
- **CR-005**: 本功能 MUST 指定 Harness 自身的单元测试、集成测试、端到端流水线测试、安全扫描和依赖扫描要求。
- **CR-006**: 本功能 MUST 声明长任务恢复、单模块生成耗时、并发流水线和测试执行反馈的性能基线。
- **CR-007**: 本功能 MUST 输出需求、架构、数据模型、API/CLI 契约、测试报告、部署/运维说明和最终执行报告。
- **CR-008**: 本功能 MUST 更新 README.md 开发记录，说明本次规格创建和后续实现的追踪方式。
- **CR-009**: 本功能 MUST 以独立 GitHub 提交完成规格创建，提交信息遵循 `[模块] 动作：内容`。
- **CR-010**: 本功能 MUST 严格执行 `specify`、`clarify`、`checklist`、`plan`、`tasks`、`implement` 顺序。

### Key Entities *(include if feature involves data)*

- **开发任务**: 表示一次从业务目标到交付产物的长运行开发流水线。
- **阶段执行记录**: 表示 Spec Kit、代码生成、测试、构建、提交和报告阶段的状态、输入、输出、耗时和错误。
- **任务清单项**: 表示 `tasks.md` 中可执行、可验证、可更新状态的最小工作单元。
- **工具调用记录**: 表示 Harness 调用脚本、测试、构建、Git、扫描或文件编辑工具的输入输出和结果。
- **失败重试记录**: 表示某个失败的原因、修复尝试、重试次数、结果和是否进入人工确认。
- **人工确认请求**: 表示因歧义或风险触发的待确认问题、风险等级、候选方案和工程师回复。
- **交付报告**: 表示一次流水线最终输出的验收证据集合。
- **Git 提交记录**: 表示 README 更新、提交范围、提交信息、本地提交和远端推送状态。

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 给定一个新业务目标，Harness 能在一次流水线中生成完整 Spec Kit 产物，并按顺序推进到实现阶段。
- **SC-002**: 100% 的阶段执行记录包含状态、输入摘要、输出路径、耗时和错误信息。
- **SC-003**: 中断后的开发任务能在 30 秒内恢复到最近成功阶段。
- **SC-004**: 单个小功能的失败修复最多自动重试三轮，且每轮都有可审计记录。
- **SC-005**: 所有高风险操作在执行前都有人工确认请求，且确认记录可追溯。
- **SC-006**: Harness 能对一个受控演示需求自动生成代码、运行测试、更新任务状态并生成最终报告。
- **SC-007**: 验证通过的小功能 100% 更新 README 开发记录并形成规范 GitHub 提交或记录明确阻塞原因。
- **SC-008**: Harness 不把 OpenClaw Skill 商店业务规则写入核心调度模块，业务代码保持独立边界。

## Assumptions

- 本功能优先完善交付物 A 的自主开发能力；OpenClaw Skill 商店作为后续由 A 自动生成的目标业务案例。
- 当前已有的 Skill 商店实现可作为验收基准和回归对照，不代表最终必须由人工继续扩展 B。
- Harness 默认在本地仓库和受控工作区执行，不直接执行真实云端破坏性操作。
- 代码生成执行器可先以可控模板、任务上下文和本地工具调用方式实现，后续再接入更复杂的多 Agent 分工。
- GitHub 远程仓库可用；若不可用，Harness 必须记录阻塞并保留本地提交证据。
