package com.openclaw.harness.generation;

import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatchPathPolicy {

    public boolean allowed(Path workspaceRoot, List<String> allowedRoots, String targetPath) {
        if (targetPath.contains("..") || targetPath.contains(".env")) {
            return false;
        }
        Path root = workspaceRoot.toAbsolutePath().normalize();
        Path target = root.resolve(targetPath).normalize();
        if (!target.startsWith(root)) {
            return false;
        }
        return allowedRoots.stream().anyMatch(allowed -> target.startsWith(root.resolve(allowed).normalize())
                || target.equals(root.resolve(allowed).normalize()));
    }
}
