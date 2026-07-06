# Quickstart: 初始化 OpenClaw Skill 商店前后端工程骨架

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 22+
- npm 10+
- 可选：Docker Desktop，用于后续 Compose 演示

## Backend Validation

```bash
cd skill-store/backend
mvn verify
```

**Expected Outcome**:

- Spring Boot context test 通过。
- 健康检查、模块目录、统一异常、Skill 示例接口测试通过。
- 生成 `target/openclaw-skill-store-0.1.0-SNAPSHOT.jar`。

## Frontend Validation

```bash
cd skill-store/frontend
npm install
npm test
npm run build
npm audit --audit-level=high
```

**Expected Outcome**:

- Vitest 路由、页面和 API 封装测试通过。
- Vite 构建生成 `dist/`。
- high/critical 级别依赖漏洞为 0。

## Local Run

后端：

```bash
cd skill-store/backend
mvn spring-boot:run
```

前端：

```bash
cd skill-store/frontend
npm run dev
```

## API Smoke

后端启动后可访问：

```bash
curl http://localhost:8081/api/store/health
curl http://localhost:8081/api/store/modules
curl http://localhost:8081/api/store/skills/featured
```

或运行占位冒烟脚本：

```powershell
.\skill-store\infra\scripts\store-smoke.ps1
```

## Current Scope Boundary

当前阶段只验证工程骨架，不代表已实现真实注册、登录、下单、支付、上传、下载或审核。
任何真实支付、隐私数据、上传扫描、权限配置、数据库初始化和线上环境操作都必须在后续
任务中触发人工确认。
