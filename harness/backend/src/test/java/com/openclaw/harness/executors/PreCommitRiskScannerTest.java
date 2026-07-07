package com.openclaw.harness.executors;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskClassifier;
import java.util.List;
import org.junit.jupiter.api.Test;

class PreCommitRiskScannerTest {

    private final PreCommitRiskScanner scanner = new PreCommitRiskScanner(new RiskClassifier());

    @Test
    void blocksSensitivePathsAndPlainSecrets() {
        assertThat(scanner.scan(List.of(".env.production"), "").safe()).isFalse();
        assertThat(scanner.scan(List.of("README.md"), "password=123").safe()).isFalse();
        assertThat(scanner.scan(List.of("README.md"), "normal docs").safe()).isTrue();
    }
}
