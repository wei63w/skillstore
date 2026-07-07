package com.openclaw.harness.observability;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CodeDiffSummaryService {

    public String summarize(List<String> changedFiles) {
        if (changedFiles == null || changedFiles.isEmpty()) {
            return "无代码变更";
        }
        long javaFiles = changedFiles.stream().filter(path -> path.endsWith(".java")).count();
        long docs = changedFiles.stream().filter(path -> path.endsWith(".md") || path.endsWith(".yaml") || path.endsWith(".yml")).count();
        return "变更文件 %d 个，其中 Java %d 个，文档/契约 %d 个".formatted(changedFiles.size(), javaFiles, docs);
    }
}
