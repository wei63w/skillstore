# Research: 完善 Agent Dev Harness 自主开发能力

## Decision: 第一版采用文件系统持久化运行证据

**Rationale**: 现有 Harness 已有 `harness/runtime/` 目录边界，文件系统能最快满足可观测、可回放、断点恢复和本地演示要求，也便于 GitHub 提交中审查产物结构。MySQL 持久化会增加迁移、连接配置和测试容器复杂度，不适合作为本阶段前置条件。

**Alternatives considered**: MySQL 8 直接落库；SQLite 本地库；内存存储。MySQL 留作后续增强，SQLite 会引入额外迁移分支，内存存储无法满足中断恢复。

## Decision: Spec Kit 流程编排使用显式阶段状态机

**Rationale**: 规格要求严格顺序执行 `specify → clarify → checklist → plan → tasks → implement`。显式枚举阶段和状态转换比通用 DAG 更易验证、更符合当前宪章，也能明确阻断条件和恢复点。

**Alternatives considered**: 引入 BPMN/工作流引擎；自由 DAG 编排；脚本串联。工作流引擎过重，自由 DAG 容易绕过宪章顺序，脚本串联缺少结构化状态。

## Decision: 代码生成执行器先采用受控模板、任务上下文和本地工具调用

**Rationale**: 当前目标是让 Harness 能稳定执行开发闭环，而不是一次性实现复杂多 Agent 大模型路由。受控模板和任务上下文便于测试、审计和失败重试，后续可以在同一接口后接入更复杂的编码 Agent。

**Alternatives considered**: 直接接入远程 LLM API；生成任意 shell 脚本执行；完全人工编码。远程 LLM API 涉及密钥与成本门禁，任意脚本风险高，完全人工编码不能验证交付物 A。

## Decision: 命令执行器基于 JDK `ProcessBuilder`

**Rationale**: Spring Boot 后端无需新增外部依赖即可执行 Maven、npm、PowerShell、Git 等命令，并能统一捕获退出码、stdout、stderr、耗时和工作目录。实现时必须限制命令白名单或风险等级，并对高风险命令触发人工确认。

**Alternatives considered**: Apache Commons Exec；直接调用系统 shell；CI 平台 API。Commons Exec 非必要，直接 shell 字符串不利于参数化和安全审计，CI API 超出本地第一版范围。

## Decision: 自动修复策略限制为最多三轮

**Rationale**: 规格和宪章都要求三轮自动修复上限。每轮必须记录失败分类、修复输入、修改摘要和验证结果；达到上限后创建人工确认请求。

**Alternatives considered**: 无限重试；仅重试一次；按错误类型自定义次数。无限重试不可控，一次重试可能不足，自定义次数会削弱治理一致性。

## Decision: Git 提交采用提交前审计 + 规范提交信息 + 推送结果记录

**Rationale**: README 开发记录和 GitHub 增量提交是项目长期证据链。提交前检查用户未提交变更、密钥和高风险配置；提交信息使用 `[模块] 动作：内容`；推送失败时保留本地提交和阻塞原因。

**Alternatives considered**: 只生成补丁不提交；每阶段都强制提交；只在最终提交。只生成补丁不满足宪章，每阶段强制提交粒度过碎，最终提交无法反映小功能证据。

## Decision: OpenClaw Skill 商店作为目标工作区输入而非 Harness 内置领域

**Rationale**: 交付物 B 是交付物 A 的验证案例，Harness 核心必须保持通用。实现中只接受目标规格路径、工作区路径、验证命令和报告路径，不硬编码商城注册、登录、支付、上传、审核等业务规则。

**Alternatives considered**: 在 Harness 中写入 Skill Store 专用生成器；把商城模块放进 Harness 后端。两者都会破坏分层解耦原则。
