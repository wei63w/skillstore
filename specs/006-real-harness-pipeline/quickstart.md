# Quickstart: 真实模型驱动 Harness 端到端流水线

## 前置条件

- 已完成 `specify`，并已生成本功能的 `plan.md`、`tasks.md`。
- 本地具备 Java 21、Maven、Git。
- 如不配置真实模型密钥，可使用 fake/stub provider 完成本地验证。
- 真实云部署、端口开放、权限变更、密钥访问必须通过人工审批。

## 配置模型 provider

复制示例配置：

```powershell
Copy-Item harness/backend/src/main/resources/application-models.example.yml harness/backend/src/main/resources/application-models.local.yml
```

本地验证可启用 fake provider。真实 provider 只配置环境变量名，不写入真实密钥。

## 运行后端验证

```powershell
Set-Location harness/backend
mvn test
mvn verify
mvn dependency:tree -Dscope=runtime
```

## 验证模型输出校验和 diff review

1. 使用 fake provider 生成一个补丁。
2. 确认返回 `PatchReview`。
3. 确认 Schema 校验通过。
4. 确认 diff review 包含 changed files、risk level、decision。
5. 构造缺少 `fileChanges` 的模型输出，确认系统拒绝应用。

## 验证端到端流水线 dry-run

1. 准备完整 feature 目录。
2. 发起 pipeline dry-run。
3. 确认阶段包括 generate、schema validation、review、tests、scans、docker build dry-run、deployment dry-run、git submit dry-run、report。
4. 确认每个 stage 都生成 evidence path。

## 验证自动修复循环

1. 构造测试失败场景。
2. 确认系统生成 repair loop 记录。
3. 确认最多三轮。
4. 三轮后仍失败时，确认生成人工审批或确认请求。

## 验证沙箱与高危审批

1. 普通目标文件写入应允许。
2. `.env`、仓库外路径、真实密钥读取应拒绝。
3. 部署、端口、权限、线上环境操作应要求审批。

## 预期结果

- 本地无真实密钥时仍可通过 fake provider 完成验证。
- 真实 provider 缺失密钥时不会泄露密钥值，只返回配置缺失原因。
- 所有模型、工具、测试、扫描、构建、部署、Git 和报告步骤均有持久化证据。
