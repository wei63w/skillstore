# Code Generation Contract

## `CodeModelProvider`

```text
generatePatch(CodeModelRequest request) -> PatchPlan
```

**规则**:

- Provider 必须返回结构化 `PatchPlan`。
- Provider 不得直接写文件。
- Provider 不得接收明文密钥。
- Provider 不可用时必须返回明确阻塞原因。

## `CodeGenerationExecutor`

```text
generate(CodeGenerationRequest request) -> PatchApplyResult
```

**模式**:

- `dry_run`: 只生成补丁计划、风险扫描和报告，不写文件。
- `apply`: 在路径和内容扫描通过后写入允许目录。

**允许路径**:

- `skill-store/backend/`
- `skill-store/frontend/`
- `skill-store/README.md`
- `docs/`
- `infra/`

**阻断路径**:

- 仓库外路径。
- 路径穿越。
- `.env`、密钥、生产支付配置。
- Harness 核心目录，除非另一个 Harness 功能规格明确授权。

## 测试与文档候选

第一版 provider 必须至少能在补丁计划中表达后端测试候选、前端文件候选和中文文档/README 候选。真实模型 provider 后续也必须保持同等结构化输出，不得只返回自由文本。
