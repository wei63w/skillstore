package com.openclaw.harness.api;

import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.executors.GitDeliveryRecord;
import com.openclaw.harness.executors.GitSubmitExecutor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitSubmitController {

    private final GitSubmitExecutor gitSubmitExecutor;

    public GitSubmitController(GitSubmitExecutor gitSubmitExecutor) {
        this.gitSubmitExecutor = gitSubmitExecutor;
    }

    @PostMapping("/api/harness/workflows/{taskId}/git-submit")
    public ApiResponse<GitDeliveryRecord> submit(@PathVariable String taskId, @Valid @RequestBody GitSubmitRequest request) {
        return ApiResponse.ok(gitSubmitExecutor.submit(Path.of(request.workspace()).toAbsolutePath().normalize(), taskId, request.commitMessage(), request.push()));
    }

    public record GitSubmitRequest(
            @NotBlank(message = "工作区不能为空")
            String workspace,
            @NotBlank(message = "提交信息不能为空")
            String commitMessage,
            boolean push
    ) {
    }
}
