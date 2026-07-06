# Data Model: OpenClaw Skill 商店交易闭环

## UserAccount

- `id`: 用户 ID
- `username`: 唯一用户名
- `email`: 邮箱
- `passwordHash`: 加盐哈希密码
- `role`: `BUYER`、`CREATOR`、`ADMIN`
- `status`: `ACTIVE`
- Validation: 用户名唯一；密码不返回；角色必须合法

## AuthSession

- `token`: 演示访问令牌
- `userId`: 关联用户
- `createdAt`: 创建时间
- Validation: token 必须存在且关联活跃用户

## SkillPackage

- `id`: 包 ID
- `originalFilename`: 原始文件名
- `sizeBytes`: 文件大小
- `storagePath`: 本地保存路径
- Validation: 后缀为 `.zip`、`.json`、`.txt`；大小 1..1048576 bytes

## SkillListing

- `id`: Skill ID
- `creatorId`: 创作者
- `name`: 名称
- `summary`: 摘要
- `category`: 分类
- `pricingMode`: `FREE`、`ONE_TIME`、`SUBSCRIPTION`
- `priceCents`: 价格分
- `packageId`: 上传包
- `auditStatus`: `PENDING_REVIEW`、`APPROVED`、`REJECTED`
- `reviewReason`: 审核备注

## StoreOrder

- `id`: 订单 ID
- `orderNo`: 订单号
- `buyerId`: 买家
- `skillId`: Skill
- `amountCents`: 金额
- `paymentStatus`: `PENDING`、`PAID`、`FAILED`

## PurchaseGrant

- `id`: 授权 ID
- `buyerId`: 买家
- `skillId`: Skill
- `orderId`: 订单
- `downloadToken`: 下载授权占位 token

## AuditLog

- `id`: 日志 ID
- `actorId`: 操作人，可为空表示匿名注册尝试
- `action`: 动作
- `targetType`: 目标类型
- `targetId`: 目标 ID
- `message`: 说明
- `createdAt`: 时间
