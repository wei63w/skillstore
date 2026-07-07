package com.openclaw.harness.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclaw.harness.gates.RiskLevel;
import com.openclaw.harness.generation.FileChange;
import com.openclaw.harness.generation.PatchPlan;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HttpCodeModelProvider implements CodeModelProvider {

    private final ModelProviderSettings settings;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public HttpCodeModelProvider() {
        this(new ModelProviderSettings(), new ObjectMapper(), HttpClient.newHttpClient());
    }

    public HttpCodeModelProvider(ModelProviderSettings settings, ObjectMapper objectMapper, HttpClient httpClient) {
        this.settings = settings;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    @Override
    public String providerKey() {
        return "http";
    }

    @Override
    public CodeModelResponse generatePatch(CodeModelRequest request) {
        if ("fake".equals(request.providerKey())) {
            return fakePatch(request);
        }
        ModelProviderConfig config = settings.requireEnabled(request.providerKey());
        String apiKey = System.getenv(config.apiKeyEnv());
        if (apiKey == null || apiKey.isBlank()) {
            return blocked("缺少模型密钥环境变量: " + config.apiKeyEnv());
        }

        List<String> errors = new ArrayList<>();
        int attempts = Math.max(1, config.maxRetries() + 1);
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                HttpRequest httpRequest = buildRequest(config, apiKey, request);
                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    errors.add("provider=%s httpStatus=%d attempt=%d".formatted(config.providerKey(), response.statusCode(), attempt));
                    continue;
                }
                String text = extractModelText(config.providerKey(), response.body());
                PatchPlan patchPlan = objectMapper.readValue(extractJsonObject(text), PatchPlan.class);
                return new CodeModelResponse(true, patchPlan, List.of());
            } catch (IOException exception) {
                errors.add("provider=%s ioError=%s attempt=%d".formatted(config.providerKey(), redact(exception.getMessage()), attempt));
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return blocked("模型调用被中断: " + config.providerKey());
            } catch (RuntimeException exception) {
                errors.add("provider=%s parseError=%s attempt=%d".formatted(config.providerKey(), redact(exception.getMessage()), attempt));
            }
        }
        return new CodeModelResponse(false, null, errors);
    }

    private HttpRequest buildRequest(ModelProviderConfig config, String apiKey, CodeModelRequest request) throws IOException {
        String prompt = buildPrompt(request);
        String body;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(config.endpoint()))
                .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                .header("Content-Type", "application/json");
        if ("claude".equals(config.providerKey())) {
            builder.header("x-api-key", apiKey).header("anthropic-version", "2023-06-01");
            body = objectMapper.writeValueAsString(Map.of(
                    "model", config.modelName(),
                    "max_tokens", 4096,
                    "messages", List.of(Map.of("role", "user", "content", prompt))
            ));
        } else {
            builder.header("Authorization", "Bearer " + apiKey);
            if ("deepseek".equals(config.providerKey())) {
                body = objectMapper.writeValueAsString(Map.of(
                        "model", config.modelName(),
                        "messages", List.of(Map.of("role", "user", "content", prompt)),
                        "response_format", Map.of("type", "json_object")
                ));
            } else {
                body = objectMapper.writeValueAsString(Map.of(
                        "model", config.modelName(),
                        "input", prompt,
                        "text", Map.of("format", Map.of("type", "json_object"))
                ));
            }
        }
        return builder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    private String buildPrompt(CodeModelRequest request) {
        return """
                你是 Agent Dev Harness 的代码生成模型。只返回一个 PatchPlan JSON 对象，不要输出 Markdown。
                字段必须匹配 Java record: planId, taskId, summary, fileChanges, riskLevel, sourceRefs。
                fileChanges 元素字段: path, operation, content, taskId, rationale。
                riskLevel 只能是 LOW, MEDIUM, HIGH, CRITICAL。

                Objective:
                %s

                Spec:
                %s

                Plan:
                %s

                Tasks:
                %s

                Contracts:
                %s

                Constraints:
                %s
                """.formatted(
                request.objective(),
                request.context().specSummary(),
                request.context().planSummary(),
                String.join("\n", request.context().taskItems()),
                String.join("\n", request.context().contractRefs()),
                String.join("\n", request.constraints())
        );
    }

    private String extractModelText(String providerKey, String body) throws IOException {
        JsonNode root = objectMapper.readTree(body);
        if ("deepseek".equals(providerKey)) {
            return root.at("/choices/0/message/content").asText();
        }
        if ("claude".equals(providerKey)) {
            return root.at("/content/0/text").asText();
        }
        if (root.hasNonNull("output_text")) {
            return root.path("output_text").asText();
        }
        JsonNode output = root.path("output");
        if (output.isArray()) {
            for (JsonNode item : output) {
                JsonNode content = item.path("content");
                if (content.isArray()) {
                    for (JsonNode block : content) {
                        if (block.hasNonNull("text")) {
                            return block.path("text").asText();
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("模型响应中未找到文本输出");
    }

    private String extractJsonObject(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("模型输出不是 JSON 对象");
        }
        return text.substring(start, end + 1);
    }

    private CodeModelResponse fakePatch(CodeModelRequest request) {
        PatchPlan plan = new PatchPlan(
                "http-fake-" + Math.abs(request.objective().hashCode()),
                request.objective(),
                "fake HTTP provider 生成的可校验补丁计划",
                List.of(new FileChange(
                        "skill-store/README.md",
                        "modify",
                        "\n## Harness 真实模型流水线记录\n\n- " + request.objective() + "\n",
                        request.objective(),
                        "记录真实模型 provider dry-run 结果"
                )),
                RiskLevel.LOW,
                List.of("spec.md", "plan.md", "tasks.md")
        );
        return new CodeModelResponse(true, plan, List.of());
    }

    private CodeModelResponse blocked(String reason) {
        return new CodeModelResponse(false, null, List.of(reason));
    }

    private String redact(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("sk-[A-Za-z0-9_-]+", "sk-***")
                .replaceAll("(?i)(api[_-]?key|authorization|x-api-key)=?[^\\s,]+", "$1=***");
    }
}
