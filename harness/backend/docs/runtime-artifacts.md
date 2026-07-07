# 运行产物约定

Harness 运行产物默认位于 `harness/runtime/`。

- `tasks/`：任务状态、断点和恢复引用。
- `logs/`：结构化执行日志。
- `context/`：上下文摘要。
- `artifacts/`：需求、架构、源码、测试、构建和部署阶段产物。
- `reports/`：执行报告。

## 自主开发闭环新增产物

- `logs/<taskId>/tool-*.json`：工具调用摘要，包含命令、工作目录、风险等级、退出码和 stdout/stderr 路径。
- `artifacts/<taskId>/artifact-*.json`：阶段产物索引，用于报告和回放。
- `reports/<taskId>-delivery-report.json`：最终交付报告，汇总阶段、测试、扫描、重试、人工确认、Git 和剩余风险。

产物不得包含明文密钥、真实服务器地址或管理员凭据。
