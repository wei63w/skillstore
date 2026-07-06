# Research: 初始化 OpenClaw Skill 商店前后端工程骨架

## Decision: 后端采用 Java 21 + Spring Boot 3.3.7

**Rationale**: 与现有 Agent Dev Harness 的 Java 21/Spring Boot 基线一致，便于统一构建、
测试、Docker 打包和后续平台治理。Spring Boot Web、Validation、Security、Flyway 与
MyBatis-Plus 能快速形成商城 API 骨架。

**Alternatives considered**: Node NestJS 可作为长期规范备选，但当前用户技术栈偏 Java，
且 Harness 已采用 Java，因此本阶段不引入第二套后端运行时。

## Decision: 前端采用 Vue 3 + TypeScript + Element Plus

**Rationale**: Skill 商店包含大量后台表单、审核、列表、配置和看板入口，Vue 3 +
Element Plus 在中后台骨架上启动速度快，组件生态匹配度高。TypeScript 保证路由、API
封装和测试更易演进。

**Alternatives considered**: React 仍可用于后续独立前端，但本阶段选择 Vue 3 作为默认
实现，避免在骨架阶段同时维护两套前端。

## Decision: 架构采用模块化单体，不拆微服务

**Rationale**: 当前目标是可运行工程骨架和业务边界，不是独立伸缩部署。模块化单体能把
买家、创作者、平台运营、商品、订单、存储、审核、通知等边界清楚放入包结构，同时保持
构建、测试和部署简单。

**Alternatives considered**: 买家、商家、运营拆为多个服务会增加网关、鉴权、部署、观测
和事务复杂度，不适合骨架阶段。

## Decision: 数据库迁移使用 Flyway，数据访问层预留 MyBatis-Plus

**Rationale**: Flyway 让基础表结构可复现、可审计；MyBatis-Plus 适合商城 CRUD 和后续
显式 SQL 治理。当前骨架只建表和示例服务，不急于写完整 Mapper。

**Alternatives considered**: JPA 自动建表会弱化迁移可审计性；纯 SQL 手写所有 CRUD 会
放慢骨架启动。

## Decision: API 统一使用 `/api/store/**` 前缀和统一响应体

**Rationale**: 统一前缀明确商城业务 API 与 Harness API 隔离；统一响应体便于前端请求
封装、异常弹窗、测试断言和后续审计。

**Alternatives considered**: 多前缀按角色拆分会让骨架阶段路由分散；完全 REST 裸响应会
增加前端错误处理分支。

## Decision: 质量门禁覆盖后端 verify、前端 test/build 和 npm audit

**Rationale**: 骨架阶段先固定最小可自动验证集合。后端用 JUnit/MockMvc 验证上下文、
健康检查、模块目录、异常和示例接口；前端用 Vitest 验证路由、页面和 API 封装；依赖
审计阻断 high/critical 漏洞。

**Alternatives considered**: 完整 E2E、SAST 和压测等留给业务闭环阶段，避免当前骨架阶段
为了未实现流程编写虚假测试。
