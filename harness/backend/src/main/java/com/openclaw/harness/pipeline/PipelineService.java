package com.openclaw.harness.pipeline;

import com.openclaw.harness.deployment.DeploymentPlanner;
import com.openclaw.harness.deployment.DockerBuildPlanner;
import com.openclaw.harness.executors.CommandExecutor;
import com.openclaw.harness.executors.CommandResult;
import com.openclaw.harness.security.SecurityScanPlanner;
import com.openclaw.harness.workflow.WorkflowStateStore;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    private final WorkflowStateStore stateStore;
    private final SecurityScanPlanner securityScanPlanner;
    private final DockerBuildPlanner dockerBuildPlanner;
    private final DeploymentPlanner deploymentPlanner;
    private final CommandExecutor commandExecutor;

    public PipelineService(WorkflowStateStore stateStore, SecurityScanPlanner securityScanPlanner, DockerBuildPlanner dockerBuildPlanner, DeploymentPlanner deploymentPlanner) {
        this(stateStore, securityScanPlanner, dockerBuildPlanner, deploymentPlanner, new CommandExecutor());
    }

    @Autowired
    public PipelineService(WorkflowStateStore stateStore, SecurityScanPlanner securityScanPlanner, DockerBuildPlanner dockerBuildPlanner, DeploymentPlanner deploymentPlanner, CommandExecutor commandExecutor) {
        this.stateStore = stateStore;
        this.securityScanPlanner = securityScanPlanner;
        this.dockerBuildPlanner = dockerBuildPlanner;
        this.deploymentPlanner = deploymentPlanner;
        this.commandExecutor = commandExecutor;
    }

    public PipelineRun dryRun(String runId, Path featureDirectory, Path workspaceRoot) {
        List<PipelineStageResult> stages = new ArrayList<>();
        for (PipelineStageType type : PipelineStageType.values()) {
            stages.add(stage(runId, type, type == PipelineStageType.DEPLOY_DRY_RUN ? PipelineStageStatus.WAITING_FOR_APPROVAL : PipelineStageStatus.PASSED));
        }
        securityScanPlanner.sastCommand(workspaceRoot.toString());
        dockerBuildPlanner.dryRunCommand("skill-store");
        deploymentPlanner.dryRun(runId, workspaceRoot.toString());
        PipelineRun run = new PipelineRun(runId, featureDirectory.toString(), workspaceRoot.toString(),
                PipelineRunStatus.WAITING_FOR_APPROVAL, PipelineStageType.DEPLOY_DRY_RUN, 0,
                reportPath(workspaceRoot, runId).toString(), List.copyOf(stages));
        stateStore.write(Path.of(run.reportPath()), run);
        return run;
    }

    public PipelineRun run(String runId, Path featureDirectory, Path workspaceRoot) {
        List<PipelineStageResult> stages = new ArrayList<>();
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.GENERATE, PipelineStageStatus.PASSED, "代码生成阶段由 CodeGenerationExecutor/模型 provider 执行，本 pipeline 记录真实执行入口。", null));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.VALIDATE_SCHEMA, PipelineStageStatus.PASSED, "PatchPlan Schema 校验已在代码生成执行器中完成。", null));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.REVIEW_DIFF, PipelineStageStatus.WAITING_FOR_APPROVAL, "diff review 需要人工确认后才能 apply。", null));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.APPLY_PATCH, PipelineStageStatus.SKIPPED, "默认不自动应用补丁；需要审批后由受控补丁执行器处理。", null));

        stages.add(commandStage(workspaceRoot, workspaceRoot.resolve("skill-store/backend"), runId, PipelineStageType.BACKEND_TEST, List.of("mvn", "test")));
        boolean blocked = hasFailure(stages);
        stages.add(blocked ? blockedStage(workspaceRoot, runId, PipelineStageType.FRONTEND_TEST, "后端测试失败，跳过前端测试。")
                : commandStage(workspaceRoot, workspaceRoot.resolve("skill-store/frontend"), runId, PipelineStageType.FRONTEND_TEST, List.of("npm", "test")));
        blocked = hasFailure(stages);
        stages.add(blocked ? blockedStage(workspaceRoot, runId, PipelineStageType.E2E_TEST, "前置测试失败，跳过 E2E。")
                : optionalPackageScriptStage(workspaceRoot, workspaceRoot.resolve("skill-store/frontend"), runId, PipelineStageType.E2E_TEST, "test:e2e"));
        blocked = hasFailure(stages);
        stages.add(blocked ? blockedStage(workspaceRoot, runId, PipelineStageType.SAST, "前置测试失败，跳过 SAST。")
                : sastStage(workspaceRoot, runId));
        blocked = hasFailure(stages);
        stages.add(blocked ? blockedStage(workspaceRoot, runId, PipelineStageType.DEPENDENCY_SCAN, "SAST 失败，跳过依赖扫描。")
                : commandStage(workspaceRoot, workspaceRoot.resolve("skill-store/frontend"), runId, PipelineStageType.DEPENDENCY_SCAN, List.of("npm", "audit", "--audit-level=high")));
        blocked = hasFailure(stages);
        stages.add(blocked ? blockedStage(workspaceRoot, runId, PipelineStageType.DOCKER_BUILD, "安全或依赖扫描失败，阻断 Docker 构建。")
                : optionalDockerStage(workspaceRoot, runId));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.DEPLOY_DRY_RUN, PipelineStageStatus.WAITING_FOR_APPROVAL, "部署、端口、权限和线上环境变更属于高危操作，等待人工审批。", null));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.GIT_SUBMIT, PipelineStageStatus.WAITING_FOR_APPROVAL, "Git commit/push 由人工审批或显式 GitSubmitExecutor 调用执行。", null));
        stages.add(writeTextStage(workspaceRoot, runId, PipelineStageType.REPORT, PipelineStageStatus.PASSED, "pipeline 报告已生成。", null));

        PipelineRunStatus status = hasFailure(stages) ? PipelineRunStatus.FAILED : PipelineRunStatus.WAITING_FOR_APPROVAL;
        PipelineRun run = new PipelineRun(runId, featureDirectory.toString(), workspaceRoot.toString(), status,
                stages.get(stages.size() - 1).stageType(), 0, reportPath(workspaceRoot, runId).toString(), List.copyOf(stages));
        stateStore.write(Path.of(run.reportPath()), run);
        return run;
    }

    public List<PipelineRun> list(Path workspaceRoot) {
        Path reports = workspaceRoot.resolve("harness/runtime/reports");
        if (!Files.isDirectory(reports)) {
            return List.of();
        }
        try (var stream = Files.list(reports)) {
            return stream
                    .filter(path -> path.getFileName().toString().endsWith("-pipeline.json"))
                    .sorted(Comparator.reverseOrder())
                    .map(path -> stateStore.read(path, PipelineRun.class))
                    .flatMap(Optional::stream)
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("读取 pipeline 列表失败", exception);
        }
    }

    public Optional<PipelineRun> find(Path workspaceRoot, String runId) {
        return stateStore.read(reportPath(workspaceRoot, runId), PipelineRun.class);
    }

    public String logs(Path workspaceRoot, String runId) {
        return find(workspaceRoot, runId)
                .map(run -> run.stages().stream()
                        .map(stage -> readEvidence(Path.of(stage.evidencePath())))
                        .reduce("", (left, right) -> left + "\n" + right))
                .orElse("");
    }

    private PipelineStageResult stage(String runId, PipelineStageType type, PipelineStageStatus status) {
        Instant now = Instant.now();
        return new PipelineStageResult("%s-%s".formatted(runId, type.name().toLowerCase()), runId, type, status,
                now, now, "harness/runtime/artifacts/%s-%s.txt".formatted(runId, type.name().toLowerCase()), null);
    }

    private PipelineStageResult commandStage(Path workspaceRoot, Path workingDirectory, String runId, PipelineStageType type, List<String> command) {
        if (!Files.isDirectory(workingDirectory)) {
            return writeTextStage(workspaceRoot, runId, type, PipelineStageStatus.SKIPPED, "工作目录不存在，阶段跳过: " + workingDirectory, null);
        }
        CommandResult result = commandExecutor.execute(workspaceRoot, workingDirectory, command);
        String body = """
                command: %s
                workingDirectory: %s
                exitCode: %d
                stdout:
                %s
                stderr:
                %s
                """.formatted(String.join(" ", command), result.workingDirectory(), result.exitCode(), result.stdout(), result.stderr());
        return writeTextStage(workspaceRoot, runId, type, result.successful() ? PipelineStageStatus.PASSED : PipelineStageStatus.FAILED,
                body, result.successful() ? null : "命令执行失败，exitCode=" + result.exitCode());
    }

    private PipelineStageResult optionalPackageScriptStage(Path workspaceRoot, Path frontendDirectory, String runId, PipelineStageType type, String script) {
        Path packageJson = frontendDirectory.resolve("package.json");
        if (!Files.exists(packageJson)) {
            return writeTextStage(workspaceRoot, runId, type, PipelineStageStatus.SKIPPED, "未找到 package.json，阶段跳过。", null);
        }
        String content = readEvidence(packageJson);
        if (!content.contains("\"" + script + "\"")) {
            return writeTextStage(workspaceRoot, runId, type, PipelineStageStatus.SKIPPED, "未配置 npm script: " + script, null);
        }
        return commandStage(workspaceRoot, frontendDirectory, runId, type, List.of("npm", "run", script));
    }

    private PipelineStageResult optionalDockerStage(Path workspaceRoot, String runId) {
        if (!Files.exists(workspaceRoot.resolve("docker-compose.yml"))) {
            return writeTextStage(workspaceRoot, runId, PipelineStageType.DOCKER_BUILD, PipelineStageStatus.SKIPPED, "未找到 docker-compose.yml，Docker 构建跳过。", null);
        }
        return commandStage(workspaceRoot, workspaceRoot, runId, PipelineStageType.DOCKER_BUILD, List.of("docker", "compose", "config"));
    }

    private PipelineStageResult sastStage(Path workspaceRoot, String runId) {
        Pattern pattern = Pattern.compile("(?i)(sk-[a-z0-9_-]{12,}|password\\s*=|api[_-]?key\\s*=|private key|Runtime\\.getRuntime)");
        List<Path> roots = List.of(workspaceRoot.resolve("skill-store"), workspaceRoot.resolve("harness/backend/src/main/java"));
        List<String> findings = new ArrayList<>();
        for (Path root : roots) {
            if (!Files.isDirectory(root)) {
                continue;
            }
            try (var stream = Files.walk(root)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> !path.toString().contains("target"))
                        .filter(path -> !path.toString().contains("node_modules"))
                        .forEach(path -> scanFile(pattern, path, findings));
            } catch (IOException exception) {
                findings.add("扫描目录失败: " + root + " " + exception.getMessage());
            }
        }
        if (findings.isEmpty()) {
            return writeTextStage(workspaceRoot, runId, PipelineStageType.SAST, PipelineStageStatus.PASSED, "SAST 基础敏感模式扫描通过。", null);
        }
        return writeTextStage(workspaceRoot, runId, PipelineStageType.SAST, PipelineStageStatus.FAILED,
                "SAST 发现风险:\n" + String.join("\n", findings), "SAST 发现高危敏感模式");
    }

    private void scanFile(Pattern pattern, Path path, List<String> findings) {
        try {
            int lineNo = 0;
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                lineNo++;
                if (pattern.matcher(line).find()) {
                    findings.add(path + ":" + lineNo + " 命中敏感模式");
                }
            }
        } catch (IOException ignored) {
            // Binary or unreadable files are ignored by the lightweight text scan.
        }
    }

    private PipelineStageResult blockedStage(Path workspaceRoot, String runId, PipelineStageType type, String reason) {
        return writeTextStage(workspaceRoot, runId, type, PipelineStageStatus.SKIPPED, reason, reason);
    }

    private PipelineStageResult writeTextStage(Path workspaceRoot, String runId, PipelineStageType type, PipelineStageStatus status, String body, String failureSummary) {
        Instant now = Instant.now();
        Path evidencePath = workspaceRoot.resolve("harness/runtime/artifacts/%s-%s.txt".formatted(runId, type.name().toLowerCase()));
        try {
            Files.createDirectories(evidencePath.getParent());
            Files.writeString(evidencePath, body == null ? "" : body, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("写入阶段证据失败: " + evidencePath, exception);
        }
        return new PipelineStageResult("%s-%s".formatted(runId, type.name().toLowerCase()), runId, type, status,
                now, now, evidencePath.toString(), failureSummary);
    }

    private boolean hasFailure(List<PipelineStageResult> stages) {
        return stages.stream().anyMatch(stage -> stage.status() == PipelineStageStatus.FAILED);
    }

    private Path reportPath(Path workspaceRoot, String runId) {
        return workspaceRoot.resolve("harness/runtime/reports/%s-pipeline.json".formatted(runId));
    }

    private String readEvidence(Path path) {
        if (!Files.exists(path)) {
            return "";
        }
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return "读取证据失败: " + exception.getMessage();
        }
    }
}
