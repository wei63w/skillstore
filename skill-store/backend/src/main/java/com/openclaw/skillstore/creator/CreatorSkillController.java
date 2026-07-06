package com.openclaw.skillstore.creator;

import com.openclaw.skillstore.audit.AuditLogService;
import com.openclaw.skillstore.auth.AuthService;
import com.openclaw.skillstore.auth.StoreRole;
import com.openclaw.skillstore.common.ApiResponse;
import com.openclaw.skillstore.common.DemoStoreState;
import com.openclaw.skillstore.skill.PricingMode;
import com.openclaw.skillstore.skill.SkillListingMapper;
import com.openclaw.skillstore.skill.SkillListingResponse;
import com.openclaw.skillstore.storage.SkillPackageStorageService;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CreatorSkillController {

    private final AuthService authService;
    private final DemoStoreState state;
    private final SkillPackageStorageService storageService;
    private final AuditLogService auditLogService;

    public CreatorSkillController(
            AuthService authService,
            DemoStoreState state,
            SkillPackageStorageService storageService,
            AuditLogService auditLogService) {
        this.authService = authService;
        this.state = state;
        this.storageService = storageService;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/api/store/creator/skills")
    public ResponseEntity<ApiResponse<SkillListingResponse>> uploadSkill(
            @RequestHeader(AuthService.TOKEN_HEADER) String token,
            @RequestParam String name,
            @RequestParam String summary,
            @RequestParam String category,
            @RequestParam PricingMode pricingMode,
            @RequestParam int priceCents,
            @RequestParam MultipartFile file) {
        DemoStoreState.UserAccount creator = authService.requireRole(token, StoreRole.CREATOR);
        DemoStoreState.SkillPackage skillPackage = storageService.save(file);
        DemoStoreState.SkillListing skill = state.saveSkill(new DemoStoreState.SkillListing(
                state.nextId("skill"),
                creator.id(),
                name,
                summary,
                category,
                pricingMode,
                priceCents,
                skillPackage.id(),
                DemoStoreState.AuditStatus.PENDING_REVIEW,
                null,
                Instant.now()));
        auditLogService.record(creator.id(), "UPLOAD_SKILL", "SKILL", skill.id(), "创作者提交Skill审核");
        return ResponseEntity.status(201).body(ApiResponse.created(SkillListingMapper.toResponse(skill)));
    }
}
