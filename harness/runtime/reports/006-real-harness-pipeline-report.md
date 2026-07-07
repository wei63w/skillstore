# 006 真实模型端到端流水线实现报告

## 结论

Harness 已完成真实代码模型端到端流水线第一版可验证实现。当前版本提供真实 provider 配置占位、fake HTTP provider、本地可复现模型输出、Schema 校验、补丁 diff review、pipeline dry-run、安全/依赖/Docker/部署阶段规划、权限沙箱、审批请求和 REST 入口。

## 已交付能力

- 模型配置：`application-models.example.yml` 提供 `stub`、`openai`、`anthropic` 占位配置，仅保存环境变量名。
- 模型安全：`ModelProviderConfig` 禁止明文密钥；`ModelOutputSchemaValidator` 校验模型输出结构。
- 代码审阅：`PatchReviewService` 生成 diff review，非法 Schema 拒绝，高危风险进入人工门禁候选。
- 真实 provider 骨架：`HttpCodeModelProvider` 作为 fake HTTP provider，验证真实 provider 接入边界。
- 流水线：`PipelineService` 生成 generate、validate、review、apply、test、E2E、SAST、dependency、Docker、deploy、Git、report 阶段。
- 修复循环：`RepairLoopService` 按最多三轮规则输出下一步动作。
- 安全和部署：`SecurityScanPlanner`、`DockerBuildPlanner`、`DeploymentPlanner` 提供 dry-run 与审批前置。
- 沙箱审批：`SandboxPolicyService`、`ApprovalRequestService` 支持 allow/deny/require approval。
- REST 入口：新增 patch review、pipeline start、sandbox evaluate 接口。

## 验证结果

- `mvn test`：通过，69 个测试，0 failures，0 errors，0 skipped。
- `mvn verify`：通过，69 个测试，Jar 构建成功，JaCoCo 报告生成。
- `mvn dependency:tree -Dscope=runtime`：通过，运行时依赖树可解析。

## 安全说明

- 未提交真实 API Key、数据库凭据、支付凭据、管理员密码或云服务器密钥。
- 真实远程部署仍默认阻塞在审批/dry-run 阶段。
- 外部 SAST/漏洞数据库插件尚未接入，本阶段提供 planner 与阻断接口。

## 后续建议

- 将 `HttpCodeModelProvider` 替换为真实 OpenAI/Claude/Gemini/local provider 适配器。
- 将模型输出从对象级校验升级为 JSON Schema 文件校验。
- 将 pipeline dry-run 阶段逐步替换为真实测试、扫描、Docker build、部署执行。
- 增加 pipeline resume API 和并发运行状态查询。
