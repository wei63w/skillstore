# Agent Dev Harness 自主开发闭环

本文档记录 Harness 自动执行 Spec Kit 流程、任务执行、测试重试、风险门禁、报告和 GitHub 交付证据的第一版设计。

## 当前范围

- 按 `specify -> clarify -> checklist -> plan -> tasks -> implement` 顺序编排功能开发。
- 使用 `harness/runtime/` 持久化阶段状态、工具调用、上下文摘要、重试记录和报告。
- 使用本地受控命令执行器调用 Maven、npm、Git 和安全扫描占位命令。
- 高风险命令、凭据、支付、隐私、权限、端口开放或真实部署操作必须进入人工确认门禁。

## 非目标

- 不执行真实云端破坏性部署。
- 不把 OpenClaw Skill 商店业务规则写入 Harness 核心。
- 不在本阶段实现真实多模型 Agent 集群。
