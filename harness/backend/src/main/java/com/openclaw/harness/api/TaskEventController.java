package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.observability.ExecutionEvent;
import com.openclaw.harness.observability.ExecutionEventService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskEventController {

    private final ExecutionEventService service;

    public TaskEventController(ExecutionEventService service) {
        this.service = service;
    }

    @GetMapping("/api/harness/tasks/{taskId}/events")
    public ApiResponse<List<ExecutionEvent>> listEvents(@PathVariable String taskId) {
        return ApiResponse.ok(service.findByTaskId(taskId));
    }
}
