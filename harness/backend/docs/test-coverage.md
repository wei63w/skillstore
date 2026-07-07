# 测试覆盖率占位

本阶段使用 Maven + JUnit 5 + JaCoCo 生成测试覆盖率报告。后续核心服务层目标为 100%
单元覆盖，低于 80% 时阻断打包部署。

## 2026-07-07 自主开发能力实现验证

- `mvn test`：通过。
- `mvn verify`：通过。
- 后端测试数量：38 个测试，0 failures，0 errors，0 skipped。
- 覆盖范围：Spec Kit 流水线编排、checkpoint 恢复、`tasks.md` 解析、受控代码生成、测试 profile、三轮重试、人工门禁升级、工具调用记录、产物索引、交付报告、README 记录更新、提交前风险扫描、临时 Git 仓库提交和 REST 合约。
- JaCoCo 报告：`harness/backend/target/site/jacoco/index.html`。
