package com.openclaw.harness.executors;

import com.openclaw.harness.gates.RiskClassifier;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PreCommitRiskScanner {

    private final RiskClassifier riskClassifier;

    public PreCommitRiskScanner(RiskClassifier riskClassifier) {
        this.riskClassifier = riskClassifier;
    }

    public ScanResult scan(List<String> changedFiles, String diffText) {
        if (riskClassifier.containsSensitiveChange(changedFiles)) {
            return new ScanResult(false, "变更包含敏感路径");
        }
        String diff = diffText == null ? "" : diffText.toLowerCase();
        if (diff.contains("password=") || diff.contains("secret=") || diff.contains("api_key") || diff.contains("private key")) {
            return new ScanResult(false, "变更疑似包含明文密钥");
        }
        if (diff.contains("real payment") || diff.contains("prod database")) {
            return new ScanResult(false, "变更疑似包含真实支付或生产配置");
        }
        return new ScanResult(true, "未发现提交前高风险内容");
    }

    public record ScanResult(boolean safe, String message) {
    }
}
