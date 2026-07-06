# Research: OpenClaw Skill 商店交易闭环

## Decision: 本阶段使用演示内存仓储

**Rationale**: 当前骨架尚无 Mapper 和完整 MySQL 环境，内存仓储能快速完成可运行闭环、测试主流程和角色边界。数据模型与 API 契约仍按后续持久化设计。

**Alternatives considered**: 直接接入 MyBatis-Plus 和 MySQL 会显著扩大迁移、事务和环境成本，不利于当前端到端闭环验证。

## Decision: 登录 token 使用演示随机令牌

**Rationale**: 本阶段目标是本地演示身份流和权限边界，随机 token 足以支撑测试。生产级 JWT、刷新令牌、吊销和过期策略后续独立实现。

**Alternatives considered**: 立即实现完整 JWT 会增加密钥管理和过期刷新复杂度，且当前不部署生产环境。

## Decision: 上传只保存受限文件，不执行、不解压

**Rationale**: Skill 包是高风险输入。演示阶段仅保存文件名、类型、大小和字节内容，限制后缀和大小，禁止执行用户内容。

**Alternatives considered**: 解压并扫描包内容需要沙箱和恶意脚本检测，后续安全审核功能再做。

## Decision: 模拟支付只接受 success/fail 两类结果

**Rationale**: 演示闭环需要覆盖支付成功和失败。使用明确枚举可避免真实支付凭据和外部回调风险。

**Alternatives considered**: 接入第三方沙箱支付仍涉及密钥、回调、签名和网络依赖，不适合本阶段。
