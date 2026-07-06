package com.openclaw.skillstore.api;

import com.openclaw.skillstore.common.ApiResponse;
import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreHealthController {

    @GetMapping("/api/store/health")
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.ok(Map.of(
                "status", "UP",
                "application", "openclaw-skill-store",
                "timestamp", Instant.now().toString()
        ));
    }
}
