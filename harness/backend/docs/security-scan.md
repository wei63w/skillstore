# 安全扫描占位

本阶段只建立工程骨架。后续实现必须接入 SAST、依赖漏洞扫描和密钥扫描，并在高危问题
未解决前阻断构建和部署。

## 2026-07-07 自主开发能力实现验证

- `mvn dependency:tree -Dscope=runtime`：通过，依赖树可解析。
- 提交前风险扫描：已由 `PreCommitRiskScannerTest` 覆盖敏感路径、明文密码、普通文档变更三类场景。
- 高风险命令识别：已由 `CommandExecutorTest` 覆盖 `git push`、`Remove-Item`、`mvn test` 风险等级分类。
- 说明：当前仓库尚未配置独立 SAST/漏洞数据库插件，本阶段记录依赖树和内置风险扫描结果；后续可接入 OWASP Dependency-Check 或企业 SAST。

## 2026-07-07 CodeGenerationExecutor 安全验证

- `mvn dependency:tree -Dscope=runtime`：通过，Spring Boot 3.3.7 运行时依赖树可解析。
- 路径安全：`PatchPathPolicyTest` 覆盖仓库外路径、路径穿越和非白名单目录阻断。
- 内容安全：`GenerationRiskScannerTest` 覆盖 `.env`、明文密钥和高风险文件目标阻断。
- 应用安全：`PatchApplierTest` 覆盖 dry-run 不写入和 apply 只写入允许目录。
- 模型安全边界：第一版仅通过 `CodeModelProvider` 抽象接入代码模型供应商，不在代码中硬编码 API Key、模型密钥或真实服务器地址。
