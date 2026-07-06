# API 说明

后端接口统一使用 `/api/store/**` 前缀。

- `GET /api/store/health`：健康检查。
- `GET /api/store/modules`：查询商城业务模块边界。
- `GET /api/store/skills/featured`：查询示例 Skill 商品。
- `POST /api/store/skills`：创建示例 Skill 草稿，占位接口。

交易闭环接口：

- `POST /api/store/auth/register`：注册买家、创作者或管理员演示账号。
- `POST /api/store/auth/login`：登录并返回演示 token；后续受保护接口使用 `X-Store-Token`。
- `POST /api/store/creator/skills`：创作者上传 `.zip`、`.json` 或 `.txt` Skill 包并提交审核。
- `GET /api/store/admin/reviews/pending`：管理员查询待审核 Skill。
- `POST /api/store/admin/reviews/{skillId}`：管理员通过或拒绝审核。
- `GET /api/store/market/skills`：买家查看已上架 Skill。
- `POST /api/store/orders`：买家创建待支付订单。
- `POST /api/store/orders/{orderId}/pay`：买家执行 `SUCCESS` 或 `FAIL` 模拟支付。
- `GET /api/store/buyer/purchases`：买家查询已购 Skill 和下载授权占位 token。
