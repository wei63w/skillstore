# Research: 初始化 Agent Dev Harness 工程骨架

## Decision: 第一阶段采用 Java Spring Boot 后端调度核心

**Rationale**: 用户明确技术栈为 Java + Vue/React。Harness 的核心职责是长任务调度、
状态持久化、审计日志、门禁控制和工具编排，天然适合以后端服务为中心推进。Spring
Boot 能提供成熟的验证、健康检查、配置管理、测试和后续 API 暴露能力，同时与后续
商城 Java 后端保持技术一致性。

**Alternatives considered**:

- TypeScript CLI-first：更轻，但会让 Harness 核心和用户既定 Java 后端栈分裂。
- Python workflow-first：适合快速 Agent 实验，但长期工程治理、类型约束和企业后端
  集成不如 Java 统一。

## Decision: Vue 3/React 控制台后置，骨架阶段只预留边界

**Rationale**: 规格目标是初始化 Harness 工程骨架，第一阶段应先固定调度核心、状态、
日志、门禁和工具插件约定。控制台 UI 依赖这些核心模型，过早实现会导致界面围绕尚未
稳定的模型返工。保留 `harness/console/` 边界即可满足前端技术栈适配。

**Alternatives considered**:

- 同步初始化 Vue 3 控制台：提升可视化感知，但扩大本阶段范围，增加 E2E 和构建复杂度。
- 同步初始化 React 控制台：同样扩大范围，且当前没有明确选择 React 的业务理由。

## Decision: Maven 作为后端构建入口

**Rationale**: Java Spring Boot 项目用 Maven 可以降低初始骨架复杂度，便于后续接入
测试、依赖扫描、打包和 CI。Maven 的目录结构也更容易被新开发者识别。

**Alternatives considered**:

- Gradle：更灵活，但初始认知成本较高，本阶段不需要复杂构建编排。

## Decision: 第一阶段用本地产物目录表达可观测约定，按 MySQL 8 设计状态模型

**Rationale**: 骨架阶段需要先定义可观测和断点续跑的证据结构，但尚未实现完整持久化。
使用 `harness/runtime/` 约定任务、日志、上下文和报告位置，可以让文档、契约和测试先
对齐。状态实体按 MySQL 8 持久化设计，后续实现阶段再创建迁移脚本。

**Alternatives considered**:

- 直接接入 MySQL：会把骨架初始化变成环境搭建任务，增加本阶段阻塞点。
- 只写文档不预留产物目录：无法满足可观测优先原则的落点。

## Decision: 同时定义 REST 合约和 CLI 合约

**Rationale**: Harness 后续需要被控制台、脚本和自动化流程调用。REST 适合控制台和
服务集成，CLI 适合本地验证、CI 和长运行 Agent 自动化调用。本阶段只定义合约，不写
完整实现。

**Alternatives considered**:

- 只定义 REST：不利于无 UI 的第一阶段验证。
- 只定义 CLI：后续控制台和外部系统集成会缺少稳定服务边界。

## Decision: 安全与高风险操作全部先以门禁模型表达

**Rationale**: 本阶段禁止真实云部署、真实密钥和破坏性操作。通过人工确认门禁模型先
定义风险分类、选项、确认结果和审计记录，可以让后续实现自然接入安全左移要求。

**Alternatives considered**:

- 暂不定义门禁：会让后续工具调用和部署能力缺少安全边界。
- 直接接入真实权限系统：超出骨架阶段范围，也引入不必要风险。
