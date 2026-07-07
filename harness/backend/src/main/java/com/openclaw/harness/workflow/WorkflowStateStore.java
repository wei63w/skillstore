package com.openclaw.harness.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WorkflowStateStore {

    private final ObjectMapper objectMapper;

    public WorkflowStateStore() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public <T> void write(Path path, T value) {
        try {
            Files.createDirectories(path.toAbsolutePath().getParent());
            objectMapper.writeValue(path.toFile(), value);
        } catch (IOException exception) {
            throw new IllegalStateException("写入工作流状态失败: " + path, exception);
        }
    }

    public <T> Optional<T> read(Path path, Class<T> type) {
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(path.toFile(), type));
        } catch (IOException exception) {
            throw new IllegalStateException("读取工作流状态失败: " + path, exception);
        }
    }
}
