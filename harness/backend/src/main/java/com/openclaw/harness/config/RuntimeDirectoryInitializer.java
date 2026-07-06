package com.openclaw.harness.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RuntimeDirectoryInitializer implements ApplicationRunner {

    private final RuntimePathProperties properties;

    public RuntimeDirectoryInitializer(RuntimePathProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        Path root = Path.of(properties.root() == null ? "../runtime" : properties.root());
        for (String child : java.util.List.of("tasks", "logs", "context", "artifacts", "reports")) {
            Files.createDirectories(root.resolve(child));
        }
    }
}
