# Research: 完善 Harness CodeGenerationExecutor

## Decision: 使用 `CodeModelProvider` 抽象支持多家代码大模型

**Rationale**: 自动生成可运行代码需要模型判断补丁计划、文件变更、测试失败原因和修复策略；多供应商抽象避免 Harness 绑定单一平台，也便于在供应商不可用时降级或记录阻塞。

**Alternatives considered**: 直接绑定单一 SDK；只使用模板生成；直接让执行器写自由文本文件。单一 SDK 供应商锁定，纯模板无法处理复杂代码判断，自由文本缺少安全边界。

## Decision: 第一版使用本地 stub provider 和结构化响应

**Rationale**: 当前实现阶段不能提交真实密钥，也不应在测试中依赖外部网络模型。stub provider 可验证上下文组装、补丁计划、风险扫描和应用流程；真实 provider 后续通过同一接口接入。

**Alternatives considered**: 直接接入 OpenAI/Claude/Gemini；手写固定文件。直接接入会引入密钥和网络波动，固定文件无法验证模型抽象。

## Decision: 补丁先以结构化 `PatchPlan` 和 `FileChange` 表达

**Rationale**: 结构化补丁能在写入前完成路径、敏感内容、任务映射和风险扫描，避免模型直接操作文件系统。

**Alternatives considered**: unified diff；模型直接调用文件写入。unified diff 后续可支持，但第一版结构化文件变更更易测试；直接写文件风险过高。

## Decision: dry-run 和 apply 模式分离

**Rationale**: dry-run 允许人和 Harness 先审查补丁计划；apply 只有在路径和内容扫描通过后执行。

**Alternatives considered**: 生成后立即应用。立即应用不符合风险门禁和可观测优先原则。

## Decision: 多模态后置为 `VisualModelProvider`

**Rationale**: 当前生成可运行代码主要依赖文本、代码、契约和测试日志；截图/UI 视觉评审可后续接入，不阻塞代码生成闭环。

**Alternatives considered**: 首版同时接入多模态。会扩大密钥、成本、测试和供应商适配范围。
