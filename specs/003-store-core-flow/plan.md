# Implementation Plan: OpenClaw Skill 商店交易闭环

**Branch**: `master` | **Date**: 2026-07-07 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/003-store-core-flow/spec.md`

## Summary

在现有 `skill-store/` 骨架上实现演示级真实闭环：注册/登录、创作者上传 Skill、管理员审核、买家下单、模拟支付、已购授权和审计日志。后端先使用演示内存仓储和受限文件存储目录完成可测试闭环；前端提供三角色操作面板。真实支付、真实银行卡、真实云部署、生产级会话和恶意代码执行均不在本阶段。

**Documentation Language**: 本功能所有文档默认中文。

## Technical Context

**Language/Version**: Java 21、Spring Boot 3.3.7、Vue 3、TypeScript、Node.js 22

**Primary Dependencies**: Spring Web、Spring Validation、Spring Security Crypto、JUnit 5、MockMvc、Vue Router、Pinia、Axios、Element Plus、Vitest

**Storage**: 演示内存仓储；上传文件保存到本地 `runtime/uploads/skill-store/`；Flyway 文档迁移后续对齐生产持久化

**Testing**: 后端 JUnit/MockMvc 集成测试和服务测试；前端 Vitest 页面/状态/API 测试；npm audit 依赖审计

**Target Platform**: 本地开发环境和 Docker Compose 演示环境

**Project Type**: Web application，前后端分离，后端模块化单体

**Performance Goals**: 本地 10 分钟内完成闭环演示；常规演示请求即时返回；上传文件不超过 1MB

**Constraints**: 不接入真实支付；不保存真实银行卡；不执行或解压上传文件；非授权角色必须被拒绝；所有敏感动作记录审计日志

**Scale/Scope**: 单实例演示闭环，覆盖主流程和关键失败路径；生产持久化、JWT、对象存储、病毒扫描、真实支付、压测后续拆分

## Constitution Check

- **Observability & Reproducibility**: Spec Kit 产物、任务状态、测试输出、README 记录和 Git 提交保留；敏感动作写入审计日志。
- **Human Risk Gates**: 真实支付、真实银行卡、真实云部署和执行上传文件均不做；若后续启用必须人工确认。
- **Harness/Business Decoupling**: 只修改 `skill-store/` 和 `specs/003-store-core-flow/`。
- **Security Shift-Left**: 注册登录、角色校验、上传限制、统一异常、审计日志和依赖审计纳入任务。
- **Automation Loop**: specify、clarify、checklist、plan、tasks、implement 全流程执行。
- **Testing Gates**: 后端和前端测试必须覆盖主流程和关键失败路径；高危依赖漏洞阻断提交。
- **Performance Baselines**: 限制上传大小并保持本地演示即时反馈。
- **Deployment & Rollback**: Docker 占位保留；本阶段不做真实部署。
- **Documentation**: 更新 API、数据库、安全、部署和 quickstart 文档。
- **README & GitHub Records**: README 追加开发记录并提交推送。
- **Spec Kit Workflow Gate**: 本计划前已完成 specify、clarify 和 checklist；tasks 后才 implement。

## Project Structure

### Documentation (this feature)

```text
specs/003-store-core-flow/
├── spec.md
├── checklists/
│   ├── requirements.md
│   └── security.md
├── plan.md
├── research.md
├── data-model.md
├── contracts/
│   └── openapi.yaml
├── quickstart.md
└── tasks.md
```

### Source Code

```text
skill-store/backend/src/main/java/com/openclaw/skillstore/
├── auth/
├── skill/
├── order/
├── storage/
├── audit/
└── common/

skill-store/frontend/src/
├── api/
├── pages/buyer/
├── pages/creator/
├── pages/admin/
└── stores/
```

**Structure Decision**: 继续模块化单体，在现有包内扩展真实演示闭环，避免拆服务。

## Spec Kit Workflow Evidence

- `specify`: [spec.md](./spec.md)
- `clarify`: [spec.md](./spec.md) 的 Clarifications 章节，无阻塞问题
- `checklist`: [checklists/requirements.md](./checklists/requirements.md), [checklists/security.md](./checklists/security.md)
- `plan`: [plan.md](./plan.md)
- `tasks`: [tasks.md](./tasks.md)
- `implement`: 按 `tasks.md` 执行并更新状态

## Complexity Tracking

无宪章违规。选择演示仓储是为了在本阶段完成本地闭环，生产持久化作为后续增强拆分。
