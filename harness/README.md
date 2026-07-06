# Agent Dev Harness

Agent Dev Harness 是本项目的 AI 自主开发调度核心。第一阶段只实现 Java Spring Boot
后端调度骨架，并预留 Vue 3/React 控制台边界。

## 边界

- Harness 负责 Agent 调度、任务状态、执行日志、上下文摘要、人工确认门禁、工具插件
  和执行报告。
- OpenClaw Skill 商店业务代码不得进入 Harness 核心模块。
- 本阶段不接入真实云部署、真实密钥或真实支付配置。

## 模块

- `agents/`：规划、编码、测试、构建部署、评审 Agent 占位。
- `state/`：长运行任务状态和断点引用。
- `observability/`：结构化执行事件。
- `context/`：上下文摘要模型。
- `gates/`：人工确认门禁。
- `tools/`：工具插件注册。
- `reports/`：执行报告和阶段产物。

## 本地启动

```bash
cd harness/backend
mvn spring-boot:run
```

健康检查：

```bash
curl http://localhost:8080/api/harness/health
```
