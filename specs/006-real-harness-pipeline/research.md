# Research: 真实模型驱动 Harness 端到端流水线

## Decision: 真实模型接入采用统一 provider 抽象加配置占位

**Rationale**: 现有 Harness 已有 `CodeModelProvider` 抽象。继续沿用该接口，并新增 provider 配置、真实 HTTP provider、fake provider 和启用/优先级选择，可以支持 OpenAI、Claude、Gemini、本地模型等多供应商，同时避免核心逻辑绑定某个 SDK。

**Alternatives considered**:

- 直接接入单一供应商 SDK：开发快，但破坏多供应商目标，密钥和模型格式容易耦合。
- 只保留 stub：安全且可测，但无法满足真实代码生成能力。

## Decision: 模型输出必须先通过 Schema 校验再进入 diff review

**Rationale**: 真实模型输出不稳定，必须把输出约束为可机器校验的补丁计划。Schema 校验失败时不得应用，失败原因进入自动修复上下文。

**Alternatives considered**:

- 直接解析自然语言补丁：风险高且不可复现。
- 让模型直接写文件：绕过审阅、安全扫描和路径边界，不符合宪章。

## Decision: diff review 独立于 patch apply

**Rationale**: 审阅记录需要在 dry-run、apply、reject、human gate 等场景复用。独立 `PatchReview` 可以记录 changed files、risk level、schema result、decision、reviewer 和证据路径。

**Alternatives considered**:

- 把 review 字段塞进 `PatchApplyResult`：短期少文件，但无法表达拒绝、待审批和多轮修复历史。

## Decision: 端到端流水线按 stage 编排并持久化

**Rationale**: 生成、校验、审阅、应用、测试、扫描、构建、部署、Git、报告都有独立失败模式。Stage 化便于恢复、重试、并发跟踪和报告汇总。

**Alternatives considered**:

- 一个大 service 串行调用所有执行器：实现快，但难恢复、难报告、难测试。

## Decision: 自动修复循环最多三轮

**Rationale**: 宪章已有最多三轮要求。修复循环接收失败上下文并重新请求模型生成补丁，重复失败或不可修复失败提前进入人工门禁。

**Alternatives considered**:

- 无限循环直到成功：不可控，容易消耗模型额度。
- 完全不自动修复：无法体现自主开发系统价值。

## Decision: Skill Store 作为 target workspace，不写入 Harness 核心业务规则

**Rationale**: Harness 需要能生成真实业务应用代码，但不能耦合 Skill Store 业务逻辑。通过 target profile、allowed roots、test/build command plan 来描述目标工程。

**Alternatives considered**:

- 在 Harness 中硬编码商城模块和业务流程：短期好演示，但破坏通用开发底座定位。

## Decision: 安全扫描、依赖扫描、Docker、部署优先作为命令计划和 dry-run

**Rationale**: 当前本地环境可验证命令编排和阻断逻辑；真实部署涉及端口、权限、云资源和证书，必须审批后执行。dry-run 先保证证据链完整。

**Alternatives considered**:

- 立即执行真实云部署：风险高且缺少审批配置。
- 完全后置部署执行器：无法验证端到端发布候选闭环。

## Decision: 沙箱策略覆盖文件、命令、网络、凭据、Git、部署操作

**Rationale**: 真实模型接入后，所有执行操作都必须经过统一风险决策。普通操作自动允许，高危操作进入审批，禁止操作直接拒绝。

**Alternatives considered**:

- 只扫描文件路径：不能覆盖命令、网络和部署风险。
