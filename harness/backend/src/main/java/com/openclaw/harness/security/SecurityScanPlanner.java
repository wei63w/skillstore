package com.openclaw.harness.security;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SecurityScanPlanner {

    public List<String> sastCommand(String target) {
        return List.of("echo", "SAST dry-run: " + target);
    }

    public List<String> dependencyScanCommand(String target) {
        return List.of("mvn", "dependency:tree", "-Dscope=runtime");
    }
}
