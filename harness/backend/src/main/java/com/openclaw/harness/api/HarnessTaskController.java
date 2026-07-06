package com.openclaw.harness.api;

import com.openclaw.harness.api.dto.CreateTaskRequest;
import com.openclaw.harness.api.dto.TaskResponse;
import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.state.HarnessTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HarnessTaskController {

    private final HarnessTaskService service;

    public HarnessTaskController(HarnessTaskService service) {
        this.service = service;
    }

    @PostMapping("/api/harness/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(TaskResponse.from(
                service.createTask(request.title(), request.objective(), request.requestedBy())
        )));
    }

    @GetMapping("/api/harness/tasks/{taskId}")
    public ApiResponse<TaskResponse> getTask(@PathVariable String taskId) {
        return service.findTask(taskId)
                .map(TaskResponse::from)
                .map(ApiResponse::ok)
                .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + taskId));
    }
}
