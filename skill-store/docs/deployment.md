# 部署说明

当前阶段提供 Docker 与 Compose 占位。后续应补齐后端镜像、前端静态资源镜像、
MySQL、Redis、Nginx、冒烟检查和回滚流程。

交易闭环演示运行：

- 后端启动后，上传文件默认写入 `skill-store/backend/runtime/uploads/skill-store/`。
- 该目录只用于本地演示运行产物，已加入 Git 忽略规则。
- 当前 token、订单、审核和购买授权使用内存仓储，服务重启后会清空。
- 生产部署前必须替换为持久化存储、对象存储、真实鉴权策略和安全扫描流水线。
