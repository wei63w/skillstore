package com.openclaw.skillstore.skill;

import com.openclaw.skillstore.common.ApiResponse;
import com.openclaw.skillstore.common.DemoStoreState;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkillCatalogController {

    private final SkillCatalogService service;
    private final DemoStoreState state;

    public SkillCatalogController(SkillCatalogService service, DemoStoreState state) {
        this.service = service;
        this.state = state;
    }

    @GetMapping("/api/store/skills/featured")
    public ApiResponse<List<SkillSummary>> featuredSkills() {
        return ApiResponse.ok(service.featuredSkills());
    }

    @PostMapping("/api/store/skills")
    public ResponseEntity<ApiResponse<SkillSummary>> create(@Valid @RequestBody CreateSkillRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @GetMapping("/api/store/market/skills")
    public ApiResponse<List<SkillListingResponse>> marketSkills() {
        return ApiResponse.ok(state.approvedSkills().stream().map(SkillListingMapper::toResponse).toList());
    }
}
