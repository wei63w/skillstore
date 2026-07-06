package com.openclaw.harness.agents;

import java.util.List;

public final class DefaultAgentModules {

    private DefaultAgentModules() {
    }

    public static List<AgentModuleDescriptor> create() {
        return List.of(
                module("planning", "规划 Agent", "拆解需求、生成架构和阶段计划", List.of("需求"), List.of("计划", "架构")),
                module("coding", "编码 Agent", "根据计划生成后端、前端或脚本代码", List.of("计划", "契约"), List.of("源码")),
                module("testing", "测试 Agent", "生成并执行单元、集成和契约测试", List.of("源码", "契约"), List.of("测试报告")),
                module("build-deploy", "构建部署 Agent", "执行构建、镜像、冒烟验证和回滚流程", List.of("源码"), List.of("构建记录", "部署记录")),
                module("review", "评审 Agent", "执行规范、安全和性能基线检查", List.of("源码", "报告"), List.of("评审报告"))
        );
    }

    private static AgentModuleDescriptor module(String key, String name, String responsibility, List<String> inputs, List<String> outputs) {
        return new AgentModuleDescriptor(key, name, responsibility, inputs, outputs, AgentModuleStatus.PLACEHOLDER);
    }
}
