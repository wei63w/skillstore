package com.openclaw.harness.sandbox;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclaw.harness.gates.RiskLevel;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApprovalRequestServiceTest {

    @Test
    void opensApprovalRequest() {
        ApprovalRequest request = new ApprovalRequestService().open("run-1", RiskLevel.HIGH, "部署", "是否部署?", List.of("approve", "reject"));

        assertThat(request.status()).isEqualTo("OPEN");
        assertThat(request.options()).contains("approve");
    }
}
