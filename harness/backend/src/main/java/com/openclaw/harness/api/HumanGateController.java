package com.openclaw.harness.api;

import com.openclaw.harness.api.dto.GateDecisionRequest;
import com.openclaw.harness.api.dto.HumanGateResponse;
import com.openclaw.harness.common.ApiResponse;
import com.openclaw.harness.gates.HumanGateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HumanGateController {

    private final HumanGateService service;

    public HumanGateController(HumanGateService service) {
        this.service = service;
    }

    @PostMapping("/api/harness/gates/{gateId}/decision")
    public ApiResponse<HumanGateResponse> decide(@PathVariable String gateId, @Valid @RequestBody GateDecisionRequest request) {
        return ApiResponse.ok(HumanGateResponse.from(service.decide(gateId, request.decision(), request.decidedBy(), request.comment())));
    }
}
