<!--
Sync Impact Report
Version change: 1.1.0 -> 1.2.0
Modified principles:
- V. Standardized Autonomous Automation Loop and Incremental GitHub Records -> V. Standardized Autonomous Automation Loop and Incremental GitHub Records
Added sections:
- VI. Spec Kit 严格开发流程
- Spec Kit workflow gate in Engineering Standards and Quality Gates
- Spec Kit workflow gate in Governance
Removed sections:
- None
Templates requiring updates:
- updated: .specify/templates/plan-template.md
- updated: .specify/templates/spec-template.md
- updated: .specify/templates/tasks-template.md
- not present: .specify/templates/commands/*.md
- updated: README.md
Follow-up TODOs:
- None
-->

# AI 自主开发系统 + OpenClaw Skill 商店项目宪章

## Core Principles

### I. 可观测与可复现交付优先

每一个 AI 自主开发步骤都 MUST 持久化为结构化、可回放证据：Agent 行为、输入、
输出、工具调用、代码差异、测试日志、构建记录、部署记录、耗时和校验结果。长运行
任务 MUST 支持中断恢复、断点续跑、失败重试，并能重建需求、架构说明、源码、报告、
图表和部署配置等中间产物。任何无法从版本化输入重建的代码、镜像、数据库迁移或
环境配置均不合规。

Rationale: 本项目要验证长周期 AI 自主交付能力；没有持久证据和可复现输出，自治
流程就无法被信任、调试、审计或恢复。

### II. 最小人工干预与显式风险门禁

Agent MUST 自主完成常规需求分析、架构草案、CRUD 实现、页面渲染、测试生成、
格式化、容器打包和静态部署。Agent 仅在以下场景 MUST 阻塞并请求人工确认：业务
需求存在重大歧义且多种实现均合理，高风险基础设施变更，权限或凭据变更，支付、
隐私或安全配置，三轮自动修复后仍无法解决的发布阻断缺陷或性能失败。面向人的
确认问题 MUST 结构化展示待确认事项、风险等级、可选方案和各方案后果。

Rationale: 人类工程师负责模糊或高风险决策，标准交付流程由 AI 系统端到端推进。

### III. Harness 与业务应用分层解耦

Agent Dev Harness MUST 保持为可复用开发底座，独立于 OpenClaw Skill 商店业务。
Harness 可以生成、测试、打包和部署商店应用，但 MUST NOT 把商店业务规则写入核心
调度、上下文管理、持久化、工具编排或评审模块。Harness 的必要层包括规划 Agent、
编码 Agent、测试 Agent、构建部署 Agent、评审 Agent、任务状态持久化、上下文窗口
管理、循环自检、人工交互钩子和工具插件池。业务应用代码 MUST 拥有独立的前端、
后端、数据库、缓存、测试和部署边界。

Rationale: Skill 商店是验证案例，不是自主开发平台的产品边界。

### IV. 安全左移与 Agent 沙箱操作

安全检查 MUST 贯穿生成、评审、构建和发布。生成代码 MUST 包含入参校验、异常处理、
统一返回体、参数化查询、鉴权校验、RBAC 权限隔离、文件上传保护和前端统一请求/
错误处理。系统 MUST NOT 生成或提交明文密钥、数据库地址、管理员凭据、真实银行卡
存储、不安全 SQL 拼接、未经审计的文件执行或提权型 Agent 操作。Skill 包上架前
MUST 扫描恶意脚本、高危系统调用、依赖漏洞、文件类型违规和大小违规。Agent 操作
MUST 运行在权限沙箱内，高风险操作 MUST 经过原则 II 定义的风险门禁。

Rationale: 自主编码只有在安全约束前置并持续执行时，才会带来可信的效率提升。

### V. 标准化自主闭环与增量 GitHub 记录

每个功能 MUST 经过文档化闭环：需求、架构、数据模型、API 契约、实现、单元测试、
集成测试、适用时的前端 E2E、SAST、依赖扫描、格式化、容器构建、部署、冒烟验证
和报告生成。Agent MUST 在每阶段后自检，并在失败时自动返工最多三轮；仍不达标时
MUST 请求人工确认。单元覆盖率低于 80%、高危安全问题、核心 E2E 失败或性能基线
未达标 MUST 阻断打包和部署。

每完成一个可独立验证的小功能、开发修改或修复，MUST 更新 README 的开发记录，
并 MUST 提交到 GitHub 远程仓库。提交粒度 MUST 对应一个清晰的小功能或修复，提交
信息 MUST 使用项目规范格式，例如 `[模块] 动作：内容`。如果当前环境尚未初始化 Git
或尚未配置 GitHub 远程仓库，MUST 在 README 记录该阻塞原因，并在仓库可用后补交。

Rationale: 可回放日志说明 Agent 做了什么，增量提交和 README 记录说明项目为什么
这样演进，二者共同支撑长期自治开发。

### VI. Spec Kit 严格开发流程

每一次开发、修改功能、修复缺陷或调整工程规范，MUST 按 Spec Kit 流程顺序自动执行：
`specify` 创建或更新功能规格，`clarify` 澄清需求并记录无歧义结论，`checklist` 生成
并通过需求检查清单，`plan` 创建技术实现计划与设计产物，`tasks` 生成可执行任务清单，
`implement` 按任务执行实现与验证。任何代码、配置、脚本、部署或文档变更进入实现前，
MUST 已具备对应的规格、澄清记录、检查清单、计划和任务；如果某一步因工具限制无法
执行，MUST 在 README 开发记录和对应 Spec 产物中说明阻塞原因、替代验证和补齐计划。

流程顺序 MUST 不被跳过、合并或事后静默补写。若遇到紧急修复，也 MUST 先创建最小规格、
完成澄清与检查清单，再生成计划和任务后实施；已发生的历史偏差 MUST 以显式追溯记录
补齐，并单独提交到 GitHub。`implement` 阶段 MUST 以 `tasks.md` 为执行来源，任务状态
MUST 随实现进度更新，最终验证结果 MUST 回写 README 和相关 Spec 产物。

Rationale: 本项目要验证 AI 自主开发系统，而不是只产出代码。严格的 Spec Kit 顺序让每
一次变更都有可追溯需求、澄清、计划、任务、实现和验证证据。

## Platform Architecture and Delivery Scope

项目有三类受治理交付物：

1. Agent Dev Harness Demo：分层 Agent 调度、长任务持久化、断点续跑、上下文压缩、
   工具插件池、人工确认钩子、代码格式化、测试生成、SAST、依赖扫描、Docker 构建、
   云部署集成、Git 集成、系统测试、报告和操作文档。
2. OpenClaw Skill 商店 Web Demo：买家前台、创作者后台、平台管理后台、Skill 文件
   存储分发、依赖和版本管理、沙箱权限检测、支付分账模拟、消息通知、MySQL 初始化、
   Redis 配置、自动化测试、Docker 打包、一键部署和完整业务文档。
3. 统一治理和规范：本宪章、功能规格、架构文档、数据库 ER 图、API 文档、测试报告、
   部署运维手册、README 开发记录和随代码演进的 Mermaid 图表。

Skill 商店默认技术栈为 React 或 Vue 3 前端，NestJS 或 Spring Boot 后端，MySQL 8
持久化，Redis 缓存，Docker 打包，并隔离开发、测试和演示环境。源码结构 MUST 分离
控制器、服务层、数据访问层、工具类、常量、配置、测试和部署资产。配置、密钥、
数据库设置和环境差异值 MUST 存入环境变量或配置中心，禁止写入业务源码。

Skill 商店 MUST 包含买家注册登录、分类首页、向量搜索、Skill 详情、购物车、模拟
结算、已购 Skill 下载或在线试用、订单记录、收藏、评价、密钥管理、创作者入驻、
Skill 包上传、版本管理、免费/买断/订阅定价、经营数据、收益对账模拟、平台安全
审核、违规下架、类目和首页运营、账号管理、订单管理和审计日志。

## Engineering Standards and Quality Gates

所有项目文档默认 MUST 使用中文，包括 README、需求、计划、任务、架构、数据库、
API、测试、部署、变更记录和运维手册。仅在代码标识符、第三方协议、英文专有名词、
外部 API 字段或用户明确要求时 MAY 使用英文。面向团队协作的文档 MUST 优先保证中文
可读性，并保留必要英文技术名词以避免歧义。

README.md MUST 存在，并作为项目入口文档持续维护。README MUST 至少包含项目定位、
交付物、目录结构、治理原则摘要、开发流程、运行方式占位、测试和部署占位、开发
记录、GitHub 提交规范和待办事项。每次完成小功能、开发修改或修复时，README 的
开发记录 MUST 追加日期、变更类型、影响范围、验证结果、提交状态和相关备注。

所有开发、修改和修复 MUST 在实现前完成 Spec Kit 工作流门禁：`specify`、`clarify`、
`checklist`、`plan`、`tasks`，随后才能进入 `implement`。每个功能目录 MUST 保留
`spec.md`、澄清记录或无歧义说明、`checklists/`、`plan.md`、必要设计产物和 `tasks.md`。
检查清单未通过、澄清仍有阻塞问题、计划违反宪章或任务清单不可执行时，MUST 阻断实现。

命名 MUST 语义化且一致：类名使用 PascalCase，方法和变量使用 camelCase，数据库表
和字段使用 snake_case。禁止无意义缩写。公共类、公共接口和复杂业务逻辑 MUST 有
文档注释，关键业务流程 MUST 添加简短注释说明设计意图。

测试是强制要求。核心服务层方法目标为 100% 单元覆盖。所有后端 API MUST 覆盖入参
校验、正常流程和异常返回的集成测试。商城核心流程，包括浏览、下单、购买和发布
Skill，MUST 有自动化 E2E 覆盖。测试报告 MUST 包含覆盖率、执行用例、缺陷清单和
漏洞等级。

性能基线是强制要求，除非功能明确说明不适用。Harness 单模块代码生成 MUST 目标
不超过 5 分钟，断点恢复 MUST 目标不超过 30 秒，Harness MUST 支持最多 3 条独立
开发流水线并发。Skill 商店普通查询接口 MUST 目标不超过 200 ms，复杂分页列表 MUST
目标不超过 500 ms，单实例 MUST 支持 200 并发用户无超时，首屏渲染 MUST 目标不超过
1.5 秒，大型 Skill 包按需 MUST 支持限速、断点续传和分片存储。首页分类、热门 Skill
和 Skill 详情 MUST 使用 Redis 缓存。

部署 MUST 自动化并容器化。Dockerfile MUST 使用分层构建以减小镜像体积。部署自动化
MUST 能拉取镜像、启动容器、配置 Nginx 反向代理、为联网演示启用 HTTPS/SSL、执行
冒烟检查，并在失败时回滚到上一镜像。日志 MUST 使用统一结构化格式并持久化，支持
按模块和等级检索。

## Governance

本宪章优先于冲突的功能计划、任务列表、模板和临时开发习惯。每个新规格和实现计划
MUST 显式检查可观测性、人工风险门禁、分层解耦、安全、测试、性能、部署可复现性、
中文文档、README 记录、GitHub 增量提交和 Spec Kit 严格开发流程。不合规项 MUST 在
实施前记录理由、备选方案、负责人和修复日期。

修订宪章 MUST 更新本文件，在文件顶部记录 Sync Impact Report，同步受影响的 Spec
Kit 模板，并记录版本变化。版本遵循语义化版本：MAJOR 用于删除或重新定义原则且会
使既有治理失效；MINOR 用于新增原则或实质扩展必需章节；PATCH 用于不改变义务的
澄清和文字修正。

合规评审 MUST 在六个门禁执行：specify 后、clarify 后、checklist 后、Phase 0 research
前、Phase 1 design 后、部署前。缺少必需文档、未执行 Spec Kit 任一步骤、澄清问题未
关闭、检查清单未通过、缺少 Agent 行为持久日志、README 未记录本次小功能、可用 GitHub
远程仓库但未提交、单元覆盖率低于 80%、存在高危安全问题、核心 E2E 失败、未获批准的
风险门禁操作、构建输入不可复现或部署回滚校验失败，均 MUST 阻断实现或发布。

**Version**: 1.2.0 | **Ratified**: 2026-07-06 | **Last Amended**: 2026-07-07
