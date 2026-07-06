# Data Model: 初始化 OpenClaw Skill 商店前后端工程骨架

## 目标

本数据模型描述骨架阶段已通过 Flyway 预留的最小商城实体。当前不实现完整 CRUD 闭环，
只为后续注册登录、商品浏览、订单、审核和审计提供可迁移的基础表。

## Entities

### store_user

- **Purpose**: 商城用户基础账号，覆盖买家、创作者和管理员三类角色。
- **Key Fields**: `id`、`username`、`email`、`password_hash`、`role`、`status`、
  `created_at`、`updated_at`
- **Validation Rules**: `username`、`email`、`password_hash` 不为空；`role` 使用
  `BUYER`、`CREATOR`、`ADMIN`；密码只保存哈希。
- **Relationships**: 被 `skill_item.creator_id`、`store_order.buyer_id`、
  `audit_log.operator_id` 引用。

### skill_category

- **Purpose**: Skill 商品分类。
- **Key Fields**: `id`、`name`、`slug`、`sort_order`、`enabled`、`created_at`
- **Validation Rules**: `name`、`slug` 唯一且不为空；禁用分类不在前台展示。
- **Relationships**: 被 `skill_item.category_id` 引用。

### skill_item

- **Purpose**: Skill 商品基础信息。
- **Key Fields**: `id`、`name`、`summary`、`category_id`、`creator_id`、`pricing_mode`、
  `price_cents`、`audit_status`、`created_at`、`updated_at`
- **Validation Rules**: `name`、`summary`、`category_id`、`creator_id` 不为空；
  `pricing_mode` 使用 `FREE`、`ONE_TIME`、`SUBSCRIPTION`；`price_cents` 不小于 0。
- **Relationships**: 属于一个分类和一个创作者；后续关联版本、文件和订单明细。

### store_order

- **Purpose**: 买家订单与模拟支付入口。
- **Key Fields**: `id`、`order_no`、`buyer_id`、`total_amount_cents`、`payment_status`、
  `created_at`、`updated_at`
- **Validation Rules**: `order_no` 唯一；金额不小于 0；当前阶段只保留模拟支付状态，
  不保存真实银行卡。
- **Relationships**: 属于一个买家；后续关联订单明细和支付模拟流水。

### audit_log

- **Purpose**: 平台操作、安全审核和后续审计追踪。
- **Key Fields**: `id`、`operator_id`、`action`、`target_type`、`target_id`、
  `risk_level`、`created_at`
- **Validation Rules**: `action`、`target_type`、`risk_level` 不为空；高风险操作需要
  人工确认记录。
- **Relationships**: 可关联任意业务目标；由操作用户触发。

## State Notes

- `skill_item.audit_status`: `DRAFT` → `PENDING_REVIEW` → `APPROVED` 或 `REJECTED`
  是后续审核流程的目标状态机。
- `store_order.payment_status`: `CREATED` → `PAID` 或 `CANCELLED` 是后续模拟支付流程的
  目标状态机。
- 骨架阶段不创建真实支付、真实上传扫描或真实权限闭环，只记录字段和边界。
