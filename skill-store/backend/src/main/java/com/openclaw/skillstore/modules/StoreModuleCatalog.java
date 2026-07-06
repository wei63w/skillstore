package com.openclaw.skillstore.modules;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoreModuleCatalog {

    private final List<StoreModuleDescriptor> modules;

    public StoreModuleCatalog() {
        this(defaultModules());
    }

    public StoreModuleCatalog(List<StoreModuleDescriptor> modules) {
        this.modules = List.copyOf(modules);
    }

    public static StoreModuleCatalog defaults() {
        return new StoreModuleCatalog(defaultModules());
    }

    public List<StoreModuleDescriptor> listModules() {
        return modules;
    }

    private static List<StoreModuleDescriptor> defaultModules() {
        return List.of(
                module("buyer", "买家前台", "浏览、搜索、购买和个人中心体验", "注册登录", "分类首页", "购物车", "已购 Skill"),
                module("creator", "创作者后台", "创作者入驻、Skill 上传发布和经营数据", "入驻", "上传", "版本管理", "定价"),
                module("admin", "平台运营后台", "安全审核、运营配置、账号和订单管理", "审核", "类目配置", "账号管理", "审计"),
                module("skill", "Skill 商品公共能力", "Skill 商品、版本、分类和搜索", "商品详情", "向量搜索", "依赖版本"),
                module("order", "订单与支付模拟", "购物车、订单和支付分账模拟", "订单", "模拟支付", "收益对账"),
                module("storage", "文件存储分发", "Skill 包存储、分片和下载占位", "上传限制", "断点续传", "分片"),
                module("audit", "安全审核与日志", "上架扫描、违规下架和操作审计", "安全扫描", "操作日志", "溯源"),
                module("notification", "消息通知", "站内通知和后续异步消息占位", "通知", "提醒")
        );
    }

    private static StoreModuleDescriptor module(String key, String name, String responsibility, String... capabilities) {
        return new StoreModuleDescriptor(key, name, responsibility, List.of(capabilities));
    }
}
