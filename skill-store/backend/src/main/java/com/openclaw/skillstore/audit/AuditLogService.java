package com.openclaw.skillstore.audit;

import com.openclaw.skillstore.common.DemoStoreState;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final DemoStoreState state;

    public AuditLogService(DemoStoreState state) {
        this.state = state;
    }

    public void record(String actorId, String action, String targetType, String targetId, String message) {
        state.appendAudit(new DemoStoreState.AuditEvent(
                state.nextId("audit"),
                actorId,
                action,
                targetType,
                targetId,
                message,
                Instant.now()));
    }
}
