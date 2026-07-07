# Quickstart: 真实模型执行与 Harness Console

## 1. 本地后端

```bash
cd harness/backend
mvn spring-boot:run
```

健康检查：

```bash
curl http://localhost:8080/actuator/health
```

## 2. 配置真实模型密钥

仅设置环境变量，不写入仓库文件。

```bash
export DEEPSEEK_API_KEY=sk-...
export OPENAI_API_KEY=sk-...
export ANTHROPIC_API_KEY=sk-ant-...
export CODEX_API_KEY=sk-...
```

Windows PowerShell：

```powershell
$env:DEEPSEEK_API_KEY="sk-..."
```

## 3. 启动控制台

```bash
cd harness/frontend
npm install
npm run dev
```

打开 `http://localhost:5174`。左侧选择或创建 run，中间查看阶段，右侧查看日志、审批和 diff。

## 4. 启动 pipeline

Dry-run：

```bash
curl -X POST http://localhost:8080/api/harness/pipelines \
  -H "Content-Type: application/json" \
  -d '{"featureKey":"007-real-harness-console","providerKey":"stub","targetProject":"skill-store","dryRun":true}'
```

真实执行：

```bash
curl -X POST http://localhost:8080/api/harness/pipelines \
  -H "Content-Type: application/json" \
  -d '{"featureKey":"007-real-harness-console","providerKey":"deepseek","targetProject":"skill-store","dryRun":false,"autoApply":false}'
```

## 5. Docker Compose

```bash
docker compose up --build
```

- 后端：`http://localhost:8080`
- 控制台：`http://localhost:5174`
- runtime 持久化：`harness/runtime`

## 6. 验证命令

```bash
cd harness/backend
mvn test
mvn verify

cd ../frontend
npm test
npm run build
```

## 7. 安全说明

- 未配置真实 key 时，真实 provider 必须显示不可用；`stub` 仍可用于本地演示。
- 高危部署、端口开放、权限变更、凭据处理、Git push 等操作默认需要审批。
- SAST 或依赖扫描工具不可用时，阶段状态应为环境阻断或跳过并记录原因，不能伪造成功。
