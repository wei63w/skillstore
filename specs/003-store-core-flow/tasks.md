# Tasks: OpenClaw Skill 商店交易闭环

**Input**: Design documents from `/specs/003-store-core-flow/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/openapi.yaml, quickstart.md

**Spec Kit Workflow Gate**: specify、clarify、checklist、plan 已完成；implement 必须按本任务清单执行。

## Phase 1: Setup

- [x] T001 Update README development log for the core flow feature in `README.md`
- [x] T002 [P] Add backend package directories and DTO conventions under `skill-store/backend/src/main/java/com/openclaw/skillstore/`
- [x] T003 [P] Add frontend API types and page test scaffolding under `skill-store/frontend/src/`

## Phase 2: Foundational

- [x] T004 [P] Add backend core flow integration tests in `skill-store/backend/src/test/java/com/openclaw/skillstore/flow/StoreCoreFlowControllerTest.java`
- [x] T005 [P] Add backend security failure tests in `skill-store/backend/src/test/java/com/openclaw/skillstore/flow/StoreCoreFlowSecurityTest.java`
- [x] T006 [P] Add frontend core flow tests in `skill-store/frontend/tests/core-flow.spec.ts`
- [x] T007 Implement demo store state and audit service in `skill-store/backend/src/main/java/com/openclaw/skillstore/common/DemoStoreState.java` and `skill-store/backend/src/main/java/com/openclaw/skillstore/audit/AuditLogService.java`
- [x] T008 Implement token/role helper in `skill-store/backend/src/main/java/com/openclaw/skillstore/auth/AuthService.java`

## Phase 3: User Story 1 - 用户注册登录 (P1)

- [x] T009 [US1] Implement auth DTOs in `skill-store/backend/src/main/java/com/openclaw/skillstore/auth/`
- [x] T010 [US1] Implement auth controller in `skill-store/backend/src/main/java/com/openclaw/skillstore/auth/AuthController.java`
- [x] T011 [US1] Implement frontend auth API and session store in `skill-store/frontend/src/api/storeApi.ts` and `skill-store/frontend/src/stores/sessionStore.ts`

## Phase 4: User Story 2 - 创作者上传 Skill (P2)

- [x] T012 [US2] Implement storage validation and save service in `skill-store/backend/src/main/java/com/openclaw/skillstore/storage/SkillPackageStorageService.java`
- [x] T013 [US2] Implement creator upload controller in `skill-store/backend/src/main/java/com/openclaw/skillstore/creator/CreatorSkillController.java`
- [x] T014 [US2] Implement creator page flow in `skill-store/frontend/src/pages/creator/CreatorHome.vue`

## Phase 5: User Story 3 - 管理员审核 Skill (P3)

- [x] T015 [US3] Implement admin review controller in `skill-store/backend/src/main/java/com/openclaw/skillstore/admin/AdminReviewController.java`
- [x] T016 [US3] Implement approved market listing in `skill-store/backend/src/main/java/com/openclaw/skillstore/skill/SkillCatalogController.java`
- [x] T017 [US3] Implement admin page flow in `skill-store/frontend/src/pages/admin/AdminHome.vue`

## Phase 6: User Story 4 - 买家下单支付 (P4)

- [x] T018 [US4] Implement order DTOs and service in `skill-store/backend/src/main/java/com/openclaw/skillstore/order/`
- [x] T019 [US4] Implement order controller in `skill-store/backend/src/main/java/com/openclaw/skillstore/order/OrderController.java`
- [x] T020 [US4] Implement buyer market/order/payment page flow in `skill-store/frontend/src/pages/buyer/BuyerHome.vue`

## Phase 7: Polish & Validation

- [x] T021 Update API/database/security/deployment docs in `skill-store/docs/`
- [x] T022 Run backend `mvn verify` in `skill-store/backend`
- [x] T023 Run frontend `npm test`, `npm run build`, and `npm audit --audit-level=high` in `skill-store/frontend`
- [x] T024 Run secret/real payment credential pattern check across changed files
- [x] T025 Mark all tasks complete and commit/push to GitHub with `[skill-store] 新增：交易闭环演示流程`

## Dependencies & Execution Order

Setup → Foundational tests → US1 → US2 → US3 → US4 → Polish. T004-T006 can run in parallel as failing tests before implementation.

## MVP Scope

US1 + US2 + US3 + US4 together define the minimum closed-loop demo requested by the user.
