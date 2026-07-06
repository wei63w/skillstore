# Quickstart: OpenClaw Skill 商店交易闭环

## Backend

```bash
cd skill-store/backend
mvn verify
mvn spring-boot:run
```

## Frontend

```bash
cd skill-store/frontend
npm install
npm test
npm run build
npm run dev
```

## Demo Flow

1. 注册创作者、管理员、买家。
2. 创作者登录并上传 Skill。
3. 管理员登录并通过审核。
4. 买家登录，查看已上架 Skill，创建订单。
5. 买家执行模拟支付成功。
6. 买家查看已购 Skill 和下载授权占位 token。

## Safety Boundary

本阶段不接入真实支付、不保存真实银行卡、不执行上传文件、不做真实云部署。
