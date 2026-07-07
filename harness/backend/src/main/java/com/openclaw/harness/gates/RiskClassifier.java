package com.openclaw.harness.gates;

import java.util.Collection;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class RiskClassifier {

    public RiskLevel classifyCommand(Collection<String> commandParts) {
        String command = String.join(" ", commandParts).toLowerCase(Locale.ROOT);
        if (containsAny(command, "rm -rf", "remove-item", "format", "drop database", "kubectl delete", "terraform destroy")) {
            return RiskLevel.CRITICAL;
        }
        if (containsAny(command, "chmod", "chown", "netsh", "iptables", "docker push", "git push", "deploy", "payment", "secret")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    public boolean requiresHumanGate(RiskLevel riskLevel) {
        return riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.CRITICAL;
    }

    public boolean containsSensitiveChange(Collection<String> changedPaths) {
        return changedPaths.stream()
                .map(path -> path.toLowerCase(Locale.ROOT))
                .anyMatch(path -> path.contains(".env")
                        || path.contains("secret")
                        || path.contains("credential")
                        || path.contains("payment")
                        || path.contains("kubeconfig"));
    }

    private boolean containsAny(String value, String... needles) {
        for (String needle : needles) {
            if (value.contains(needle)) {
                return true;
            }
        }
        return false;
    }
}
