# 测试覆盖率占位

本阶段使用 Maven + JUnit 5 + JaCoCo 生成测试覆盖率报告。后续核心服务层目标为 100%
单元覆盖，低于 80% 时阻断打包部署。

## 2026-07-07 自主开发能力实现验证

- `mvn test`：通过。
- `mvn verify`：通过。
- 后端测试数量：38 个测试，0 failures，0 errors，0 skipped。
- 覆盖范围：Spec Kit 流水线编排、checkpoint 恢复、`tasks.md` 解析、受控代码生成、测试 profile、三轮重试、人工门禁升级、工具调用记录、产物索引、交付报告、README 记录更新、提交前风险扫描、临时 Git 仓库提交和 REST 合约。
- JaCoCo 报告：`harness/backend/target/site/jacoco/index.html`。

## 2026-07-07 CodeGenerationExecutor 验证

- `mvn test`：通过。
- `mvn verify`：通过。
- 后端测试数量：49 个测试，0 failures，0 errors，0 skipped。
- 覆盖范围：多模型供应商注册、缺失供应商阻断、Spec Kit 上下文加载、补丁计划结构、路径白名单、敏感内容扫描、dry-run 不写入、apply 受控写入、代码生成报告落盘和 `/api/harness/code-generation` REST 合约。
- JaCoCo 报告：`harness/backend/target/site/jacoco/index.html`。

## 2026-07-07 真实模型端到端流水线验证

- `mvn test`：通过。
- `mvn verify`：通过。
- 后端测试数量：69 个测试，0 failures，0 errors，0 skipped。
- 覆盖范围：模型 provider 配置占位、密钥引用校验、模型输出 Schema 校验、补丁 diff review、fake HTTP provider、本地 pipeline dry-run、自动修复三轮决策、安全扫描命令计划、Docker build dry-run、部署 dry-run、权限沙箱、审批请求和 REST 合约。
- JaCoCo 报告：`harness/backend/target/site/jacoco/index.html`。
