package com.openclaw.skillstore.admin;

import com.openclaw.skillstore.audit.AuditLogService;
import com.openclaw.skillstore.auth.AuthService;
import com.openclaw.skillstore.auth.StoreRole;
import com.openclaw.skillstore.common.ApiResponse;
import com.openclaw.skillstore.common.DemoStoreState;
import com.openclaw.skillstore.skill.SkillListingMapper;
import com.openclaw.skillstore.skill.SkillListingResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AdminReviewController {

    private final AuthService authService;
    private final DemoStoreState state;
    private final AuditLogService auditLogService;

    public AdminReviewController(AuthService authService, DemoStoreState state, AuditLogService auditLogService) {
        this.authService = authService;
        this.state = state;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/api/store/admin/reviews/pending")
    public ApiResponse<List<SkillListingResponse>> pending(@RequestHeader(AuthService.TOKEN_HEADER) String token) {
        authService.requireRole(token, StoreRole.ADMIN);
        return ApiResponse.ok(state.pendingSkills().stream().map(SkillListingMapper::toResponse).toList());
    }

    @PostMapping("/api/store/admin/reviews/{skillId}")
    public ApiResponse<SkillListingResponse> review(
            @RequestHeader(AuthService.TOKEN_HEADER) String token,
            @PathVariable String skillId,
            @Valid @RequestBody ReviewRequest request) {
        DemoStoreState.UserAccount admin = authService.requireRole(token, StoreRole.ADMIN);
        DemoStoreState.SkillListing existing = state.findSkill(skillId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill不存在"));
        DemoStoreState.AuditStatus status = request.decision() == ReviewDecision.APPROVE
                ? DemoStoreState.AuditStatus.APPROVED
                : DemoStoreState.AuditStatus.REJECTED;
        DemoStoreState.SkillListing reviewed = new DemoStoreState.SkillListing(
                existing.id(),
                existing.creatorId(),
                existing.name(),
                existing.summary(),
                existing.category(),
                existing.pricingMode(),
                existing.priceCents(),
                existing.packageId(),
                status,
                request.reason(),
                existing.createdAt());
        state.saveSkill(reviewed);
        auditLogService.record(admin.id(), "REVIEW_SKILL", "SKILL", skillId, status.name() + ": " + request.reason());
        return ApiResponse.ok(SkillListingMapper.toResponse(reviewed));
    }
}
