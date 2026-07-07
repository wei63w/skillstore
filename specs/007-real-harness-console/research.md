# Research: 真实模型执行与 Harness Console

## 决策 1：模型 provider 采用协议适配层

**Decision**: 保留 `CodeModelProvider` 抽象，新增真实 HTTP provider 适配 DeepSeek、OpenAI、Claude、Codex。配置包含 `providerKey`、`protocol`、`endpoint`、`model`、`secretEnv`、`timeoutSeconds`、`maxRetries`、`enabled`。

**Rationale**: DeepSeek 使用 Chat Completions 风格，OpenAI 推荐 Responses API，Claude 使用 Messages API。统一成一个 provider 抽象可以让流水线不感知供应商协议差异。

**Alternatives considered**:

- 只接 DeepSeek：实现最快，但无法满足多家大模型要求。
- 直接在 executor 中写 HTTP 逻辑：会污染代码生成执行器，后续难以扩展和测试。

## 决策 2：Codex 作为 OpenAI Responses 兼容 provider

**Decision**: `codex` provider 使用 OpenAI Responses 协议，默认模型名由配置决定，例如 `gpt-5.1-codex-max` 或用户本地可用模型。

**Rationale**: 当前 Harness 需要“代码大模型”能力，不需要单独依赖 Codex CLI 的交互会话。把 Codex 建模为 OpenAI Responses provider 可以复用认证、请求和输出解析。

**Alternatives considered**:

- 调用本地 Codex CLI：更接近人工使用方式，但命令交互、权限和输出稳定性更复杂，应后置。

## 决策 3：模型输出强制 PatchPlan JSON

**Decision**: 所有 provider prompt 都要求只输出 PatchPlan JSON；后端先从响应中提取 JSON，再用既有 `ModelOutputSchemaValidator` 校验。

**Rationale**: 真实模型可能返回自然语言解释，不能直接应用。Schema 是自动代码修改的第一道安全门。

**Alternatives considered**:

- 接受 markdown diff：可读性好，但路径、风险、测试候选和文档候选难以结构化审阅。

## 决策 4：真实流水线采用命令执行器 + 阶段证据

**Decision**: PipelineService 在非 dry-run 模式下通过 `CommandExecutor` 真实执行测试、扫描、构建、Git 相关命令；每个阶段生成 `PipelineStageEvidence` 和报告文件。

**Rationale**: 既能保持 Harness 可观察，又能区分环境缺失、命令失败、安全阻断和业务测试失败。

**Alternatives considered**:

- 仍然只 dry-run：不能验证自动生成交付物 B。
- 引入 CI 服务：本地第一阶段复杂度过高，Compose 演示不需要。

## 决策 5：SAST/依赖扫描先做本地可用工具编排

**Decision**: SAST 阶段先执行敏感模式/危险调用扫描，并预留 Semgrep/Checkstyle/SpotBugs 接入点；依赖扫描执行 Maven dependency 和 npm audit（存在前端工程时）。

**Rationale**: 用户需要本地先跑起来；工具不可用时必须标记环境阻断，不伪造成功。

**Alternatives considered**:

- 强制安装商业扫描器：不可复现，阻碍本地演示。

## 决策 6：Harness Console 使用 Vue 3 轻量实现

**Decision**: 新增 `harness/frontend`，使用 Vue 3 + TypeScript + Vite。首页即工作台：左任务、中阶段、右日志/审批/diff。

**Rationale**: 与用户技术栈一致，能快速搭建中后台操作界面；不引入 Element Plus 也能降低骨架阶段依赖。

**Alternatives considered**:

- 只使用命令行：不满足可观察和审批交互要求。
- 直接复用 Skill Store 前端：会破坏 Harness 与业务商城解耦。
