package com.openclaw.harness.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "harness.runtime")
public record RuntimePathProperties(String root) {
}
