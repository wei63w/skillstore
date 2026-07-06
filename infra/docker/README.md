# Docker 构建占位

后续实现阶段可使用以下流程构建 Harness 后端镜像：

```bash
cd harness/backend
mvn verify
cd ../..
docker build -f infra/docker/harness-backend.Dockerfile -t openclaw/agent-dev-harness:demo .
```

本阶段不执行真实云部署。
