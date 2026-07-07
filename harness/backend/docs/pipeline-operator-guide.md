# Harness 真实流水线操作指南

## 能力范围

006 阶段提供真实模型端到端流水线的本地可验证骨架：

- 代码模型 provider 配置占位。
- 模型输出 Schema 校验。
- 补丁 diff review。
- pipeline dry-run 阶段编排。
- 后端、前端、E2E、安全扫描、依赖扫描、Docker 构建、部署 dry-run、Git 提交和报告阶段记录。

真实远程部署、端口开放、权限变更和密钥访问必须通过人工审批。

## 基本流程

1. 准备完整 Spec Kit feature 目录。
2. 配置 `application-models.example.yml` 中的 provider 占位。
3. 调用 `/api/harness/code-generation/reviews` 生成补丁审阅。
4. 调用 `/api/harness/pipelines` 启动 dry-run pipeline。
5. 查看 `harness/runtime/reports/{runId}-pipeline.json`。

## 运行命令

```powershell
Set-Location harness/backend
mvn test
mvn verify
```

## 风险规则

- Schema 校验失败不得 apply。
- 高危路径、`.env`、凭据访问直接阻断或进入审批。
- 部署阶段默认 `WAITING_FOR_APPROVAL`。
- 三轮自动修复后仍失败时必须进入人工确认。
