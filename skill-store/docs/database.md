# 数据库说明

数据库迁移脚本位于 `backend/src/main/resources/db/migration/`。当前骨架包含用户、
Skill、订单和审计日志的最小表结构占位。

交易闭环演示阶段使用内存仓储完成端到端流程，实体与后续持久化表保持一致：

- `UserAccount`：买家、创作者、管理员账号，密码只保存加盐哈希。
- `AuthSession`：演示 token 与用户关联。
- `SkillPackage`：上传文件名、大小和本地保存路径。
- `SkillListing`：Skill 商品、定价和审核状态。
- `StoreOrder`：订单号、金额和支付状态。
- `PurchaseGrant`：支付成功后的下载授权占位 token。
- `AuditEvent`：注册、上传、审核、下单和模拟支付审计记录。

后续生产持久化需要将这些演示实体映射到 MySQL 表，并补充事务、一致性和迁移测试。
