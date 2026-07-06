package com.openclaw.harness.tools;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ToolPluginRegistry {

    private final List<ToolPluginDescriptor> plugins;

    public ToolPluginRegistry() {
        this(defaultPlugins());
    }

    public ToolPluginRegistry(List<ToolPluginDescriptor> plugins) {
        this.plugins = List.copyOf(plugins);
    }

    public static ToolPluginRegistry defaults() {
        return new ToolPluginRegistry(defaultPlugins());
    }

    public List<ToolPluginDescriptor> listPlugins() {
        return plugins;
    }

    private static List<ToolPluginDescriptor> defaultPlugins() {
        return List.of(
                new ToolPluginDescriptor("formatter", "代码格式化", "格式化生成代码", RiskLevel.MEDIUM, true, false),
                new ToolPluginDescriptor("sast", "静态安全扫描", "扫描高危代码模式", RiskLevel.HIGH, true, true),
                new ToolPluginDescriptor("docker-build", "Docker 构建", "构建演示镜像", RiskLevel.HIGH, true, true),
                new ToolPluginDescriptor("cloud-deploy", "云部署", "执行远程部署动作", RiskLevel.CRITICAL, false, true)
        );
    }
}
