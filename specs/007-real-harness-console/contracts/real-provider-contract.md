# Real Provider Contract

## 配置规则

真实 provider 只能通过环境变量读取密钥，不允许在 YAML、README、测试 fixture 或 runtime 报告中写入明文密钥。

```yaml
harness:
  model:
    providers:
      deepseek:
        enabled: true
        protocol: OPENAI_CHAT
        endpoint: https://api.deepseek.com/chat/completions
        model: deepseek-v4-pro
        secret-env: DEEPSEEK_API_KEY
        timeout-seconds: 90
        max-retries: 2
```

## PatchPlan JSON 输出

所有真实模型最终必须返回以下结构的 JSON 对象：

```json
{
  "summary": "中文摘要",
  "changes": [
    {
      "path": "skill-store/backend/src/main/java/Example.java",
      "operation": "CREATE_OR_REPLACE",
      "content": "file content",
      "rationale": "why"
    }
  ],
  "tests": [
    {
      "command": "mvn test",
      "workingDirectory": "skill-store/backend",
      "required": true
    }
  ],
  "docs": [
    {
      "path": "README.md",
      "summary": "开发记录更新"
    }
  ],
  "risks": [
    {
      "level": "LOW",
      "description": "无高危操作"
    }
  ]
}
```

## Provider 协议

### DeepSeek

- `protocol`: `OPENAI_CHAT`
- `endpoint`: `https://api.deepseek.com/chat/completions`
- 认证头：`Authorization: Bearer ${DEEPSEEK_API_KEY}`
- 输出解析：`choices[0].message.content`
- JSON 模式：请求中设置 `response_format: {"type":"json_object"}`

### OpenAI

- `protocol`: `OPENAI_RESPONSES`
- `endpoint`: `https://api.openai.com/v1/responses`
- 认证头：`Authorization: Bearer ${OPENAI_API_KEY}`
- 输出解析：优先读取 response output 中的 `output_text`

### Claude

- `protocol`: `ANTHROPIC_MESSAGES`
- `endpoint`: `https://api.anthropic.com/v1/messages`
- 认证头：`x-api-key: ${ANTHROPIC_API_KEY}`
- 必需头：`anthropic-version`
- 输出解析：content block 中的 text

### Codex

- `protocol`: `OPENAI_RESPONSES`
- `endpoint`: 默认同 OpenAI Responses，可由配置覆盖
- `secretEnv`: `CODEX_API_KEY` 或 `OPENAI_API_KEY`
- `model`: 由本地配置指定

## 错误处理

- 缺少 `secretEnv`：阻断，错误类别 `MISSING_SECRET`
- HTTP 429：按 `maxRetries` 重试，仍失败为 `RATE_LIMITED`
- 超时：按 `maxRetries` 重试，仍失败为 `TIMEOUT`
- JSON 提取失败或 Schema 不通过：不应用补丁，进入 `INVALID_SCHEMA`
- 高危路径或敏感内容：不应用补丁，进入审批或阻断
