package com.openclaw.harness.deployment;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DockerBuildPlanner {

    public List<String> dryRunCommand(String imageName) {
        return List.of("echo", "Docker build dry-run: " + imageName);
    }
}
