# OpenClaw Skill 商店

OpenClaw Skill 商店是用于验证 Agent Dev Harness 自主开发能力的标准业务应用。商城与
Harness 保持独立边界，当前阶段只初始化前后端工程骨架。

## 边界

- `backend/`：商城后端业务服务，统一使用 `/api/store/**` 接口前缀。
- `frontend/`：商城前端应用，按买家、创作者、平台运营三个角色划分入口。
- `docs/`：架构、API、数据库、安全和部署文档。
- `infra/`：Docker、Compose 和脚本占位。

## 本地验证

```bash
cd skill-store/backend
mvn verify

cd ../frontend
npm install
npm test
npm run build
```

## 当前状态

本阶段不实现真实注册、下单、支付、上传或审核闭环；支付、文件上传、权限和安全审核仅
保留骨架边界与风险说明。

## 开发记录

| 日期 | 类型 | 范围 | 说明 | 验证结果 | GitHub 状态 |
|------|------|------|------|----------|-------------|
| 2026-07-07 | 实现 | 商城前后端骨架 | 创建 Spring Boot 3.3.7 后端、Vue 3 前端、Flyway 基础表、三角色路由、统一响应、全局异常、Docker/Compose 占位和中文文档 | 后端 `mvn test` 通过 8 个测试；前端 `npm test` 通过 5 个测试；`npm run build` 通过；`npm audit --audit-level=high` 零漏洞 | 随本次提交推送到 `origin/master` |
