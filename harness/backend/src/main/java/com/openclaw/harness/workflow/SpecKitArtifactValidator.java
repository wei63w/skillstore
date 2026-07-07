package com.openclaw.harness.workflow;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpecKitArtifactValidator {

    public List<String> missingArtifacts(Path featureDirectory, SpecKitStage stage) {
        return stage.requiredArtifacts().stream()
                .filter(required -> !Files.exists(featureDirectory.resolve(required)))
                .toList();
    }

    public boolean hasRequiredArtifacts(Path featureDirectory, SpecKitStage stage) {
        return missingArtifacts(featureDirectory, stage).isEmpty();
    }
}
