package com.openclaw.harness.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class GenerationRiskScanner {

    public List<String> scan(PatchPlan plan) {
        List<String> risks = new ArrayList<>();
        for (FileChange change : plan.fileChanges()) {
            String content = change.content().toLowerCase(Locale.ROOT);
            if (content.contains("password=") || content.contains("api_key") || content.contains("secret=") || content.contains("private key")) {
                risks.add("疑似明文密钥: " + change.path());
            }
            if (content.contains("real payment") || content.contains("prod database")) {
                risks.add("疑似真实支付或生产配置: " + change.path());
            }
        }
        return risks;
    }
}
