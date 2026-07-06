# Implementation Plan: 初始化 OpenClaw Skill 商店前后端工程骨架

**Branch**: `002-init-skill-store` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/002-init-skill-store/spec.md`

## Summary

本功能为 OpenClaw Skill 商店创建独立于 Agent Dev Harness 的前后端分离工程骨架。
已采用 Java 21 + Spring Boot 3.3.7 + Vue 3 + TypeScript 的模块化单体方案，先完成
后端调度外的商城业务边界、三角色前端入口、数据库迁移、Docker/Compose 占位、中文
文档、README 开发记录和基础质量门禁。

**Documentation Language**: 本功能所有规格、计划、任务、架构、API、数据库、安全、
部署和 README 记录默认使用中文。仅代码标识符、第三方协议、API 字段和英文专有名词
保留英文。

## Technical Context

**Language/Version**: Java 21、Spring Boot 3.3.7、Vue 3、TypeScript 5.7、Node.js 22

**Primary Dependencies**: Spring Web、Spring Validation、Spring Security、MyBatis-Plus
3.5.9、Flyway、Redis、JUnit 5、Vue Router、Pinia、Axios、Element Plus、Vite 8、
Vitest 4

**Storage**: MySQL 8 作为演示目标数据库；H2 用于测试环境；Redis 作为缓存占位；Flyway
管理基础表迁移

**Testing**: Maven Surefire/JUnit 5、Spring Boot Test、MockMvc、JaCoCo、Vitest、
Testing Library Vue、npm audit

**Target Platform**: 本地开发与 Docker Compose 演示环境；后续可接入云主机/Nginx/HTTPS
发布链路

**Project Type**: Web application，前后端分离，后端模块化单体，前端单 Vue 应用按角色
路由分区

**Performance Goals**: 骨架阶段验证构建和路由/接口可用；后续商城查询接口目标普通查询
≤200ms、复杂分页≤500ms、首屏≤1.5s、单实例 200 并发无超时

**Constraints**: 不实现真实注册、下单、支付、上传或审核闭环；不得提交真实密钥、真实
银行卡、真实云服务器地址；支付、文件上传、权限和安全审核只保留边界与风险说明

**Scale/Scope**: 当前只覆盖工程骨架、基础表、模块占位、示例接口、三角色页面、文档和
验证命令；完整商城业务闭环后续功能继续拆分

## Constitution Check

*GATE: Passed before Phase 0 research. Re-checked after Phase 1 design.*

- **Observability & Reproducibility**: README、Spec 产物、Flyway、package-lock、Dockerfile
  和验证命令已落地，可复现构建；运行日志/审计日志在后续业务实现中继续扩展。
- **Human Risk Gates**: plan、security、deployment 文档明确支付、隐私、上传、凭据、
  权限、端口开放、数据库初始化和线上销毁需人工确认。
- **Harness/Business Decoupling**: 商城代码全部位于 `skill-store/`，未写入 `harness/`
  调度核心。
- **Security Shift-Left**: 后端统一响应、全局异常、Validation、Security 占位、RBAC 枚举、
  npm audit 和安全文档已落地；真实鉴权/上传扫描后续实现。
- **Automation Loop**: 已覆盖规格、计划、数据模型、API 契约、实现、后端测试、前端测试、
  构建、依赖审计、Docker/Compose 占位、README 记录和 GitHub 提交。
- **Testing Gates**: 后端 `mvn verify` 通过 8 个测试；前端 `npm test` 通过 5 个测试；
  `npm run build` 通过；`npm audit --audit-level=high` 零漏洞。
- **Performance Baselines**: 骨架阶段只记录商城性能目标，实际压测在后续业务闭环中执行。
- **Deployment & Rollback**: 已提供 backend/frontend Dockerfile、Compose 和 smoke 脚本；
  真正云部署、Nginx、HTTPS 和回滚验证为后续任务。
- **Documentation**: 已提供 README、architecture、api、database、security、deployment；
  本目录补齐 research、data-model、contracts、quickstart、tasks。
- **README & GitHub Records**: README 已追加开发记录；实现提交已推送到 GitHub。

## Project Structure

### Documentation (this feature)

```text
specs/002-init-skill-store/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── openapi.yaml
├── tasks.md
└── checklists/
    └── requirements.md
```

### Source Code (repository root)

```text
skill-store/
├── README.md
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/openclaw/skillstore/
│       │   ├── common/
│       │   ├── config/
│       │   ├── auth/
│       │   ├── api/
│       │   ├── modules/
│       │   ├── skill/
│       │   ├── buyer/
│       │   ├── creator/
│       │   ├── admin/
│       │   ├── order/
│       │   ├── storage/
│       │   ├── audit/
│       │   └── notification/
│       ├── main/resources/db/migration/
│       └── test/
├── frontend/
│   ├── package.json
│   ├── src/
│   │   ├── app/
│   │   ├── layouts/
│   │   ├── router/
│   │   ├── stores/
│   │   ├── api/
│   │   ├── pages/buyer/
│   │   ├── pages/creator/
│   │   ├── pages/admin/
│   │   └── styles/
│   └── tests/
├── infra/
│   ├── docker/
│   ├── docker-compose.yml
│   └── scripts/
└── docs/
    ├── architecture.md
    ├── api.md
    ├── database.md
    ├── security.md
    └── deployment.md
```

**Structure Decision**: 选择 `skill-store/` 独立业务应用目录，内部前后端分离。后端先使用
模块化单体，保留买家、创作者、平台运营、商品、订单、文件、审核、通知等业务边界；
前端先使用单 Vue 3 应用，通过路由区分三类入口，避免骨架阶段拆分过早。

## Complexity Tracking

当前计划无宪章违规项。模块化单体是比微服务更简单的方案，符合骨架阶段快速验证和边界
清晰的目标。
