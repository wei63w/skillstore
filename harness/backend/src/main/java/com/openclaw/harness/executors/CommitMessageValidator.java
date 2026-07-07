package com.openclaw.harness.executors;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class CommitMessageValidator {

    private static final Pattern PATTERN = Pattern.compile("^\\[[^\\]]+] .+：.+$");

    public boolean valid(String message) {
        return message != null && PATTERN.matcher(message).matches();
    }

    public void requireValid(String message) {
        if (!valid(message)) {
            throw new IllegalArgumentException("提交信息必须符合 [模块] 动作：内容");
        }
    }
}
