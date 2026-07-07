# Contract: 真实模型驱动 Harness 端到端流水线

## Model Provider Configuration

配置文件只允许保存占位引用，不允许保存真实密钥。

```yaml
harness:
  models:
    providers:
      - providerKey: openai
        enabled: false
        priority: 10
        modelName: gpt-5-codex
        endpoint: https://api.openai.com/v1/responses
        apiKeyEnv: OPENAI_API_KEY
        timeoutSeconds: 120
        maxRetries: 2
      - providerKey: anthropic
        enabled: false
        priority: 20
        modelName: claude-code-model
        endpoint: https://api.anthropic.com/v1/messages
        apiKeyEnv: ANTHROPIC_API_KEY
        timeoutSeconds: 120
        maxRetries: 2
```

## Required Model Output

```json
{
  "planId": "patch-001",
  "taskId": "T001",
  "summary": "生成或修复目标功能",
  "riskLevel": "LOW",
  "sourceRefs": ["spec.md", "plan.md", "tasks.md"],
  "fileChanges": [
    {
      "path": "skill-store/README.md",
      "changeType": "modify",
      "content": "...",
      "taskId": "T001",
      "reason": "记录生成结果"
    }
  ]
}
```

校验规则：

- `planId`、`taskId`、`summary`、`riskLevel`、`sourceRefs`、`fileChanges` 必填。
- `riskLevel` 只能为 `LOW`、`MEDIUM`、`HIGH`、`CRITICAL`。
- `changeType` 只能为 `create`、`modify`、`delete`。
- `path` 必须位于 allowed roots 内。
- `content` 不得包含真实密钥、`.env` 内容或未授权凭据。

## Pipeline Stages

默认阶段顺序：

1. `GENERATE`
2. `VALIDATE_SCHEMA`
3. `REVIEW_DIFF`
4. `APPLY_PATCH`
5. `BACKEND_TEST`
6. `FRONTEND_TEST`
7. `E2E_TEST`
8. `SAST`
9. `DEPENDENCY_SCAN`
10. `DOCKER_BUILD`
11. `DEPLOY_DRY_RUN`
12. `GIT_SUBMIT`
13. `REPORT`

## Approval Contract

高危审批必须包含：

- 待确认内容。
- 风险等级。
- 触发原因。
- 可选方案。
- 默认建议。
- 阻塞阶段。
- 审批结果。
