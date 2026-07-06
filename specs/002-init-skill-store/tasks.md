# Tasks: 初始化 OpenClaw Skill 商店前后端工程骨架

**Input**: Design documents from `/specs/002-init-skill-store/`

**Prerequisites**: [plan.md](./plan.md), [spec.md](./spec.md), [research.md](./research.md),
[data-model.md](./data-model.md), [contracts/openapi.yaml](./contracts/openapi.yaml),
[quickstart.md](./quickstart.md)

**Documentation Language**: 本任务文件和相关项目文档默认使用中文。仅代码标识符、第三方
协议、API 字段或明确需要的英文专有名词保留英文。

**Tests**: 骨架阶段测试已采用 TDD 执行，先创建失败测试，再实现后端和前端骨架并跑通
验证。完整业务 E2E、SAST、性能报告和真实部署回滚在后续业务闭环功能中继续扩展。

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: 初始化商城独立工程目录和基础依赖。

- [x] T001 Create `skill-store/` top-level application boundary and README in `skill-store/README.md`
- [x] T002 Initialize Spring Boot Maven backend project in `skill-store/backend/pom.xml`
- [x] T003 Initialize Vue 3 TypeScript frontend project in `skill-store/frontend/package.json`
- [x] T004 [P] Configure generated artifact ignore rules in `.gitignore`
- [x] T005 [P] Create documentation and infrastructure directories in `skill-store/docs/` and `skill-store/infra/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: 建立所有用户故事共享的后端、前端、数据和验证基础。

- [x] T006 Implement Spring Boot entry point in `skill-store/backend/src/main/java/com/openclaw/skillstore/SkillStoreApplication.java`
- [x] T007 [P] Implement unified API response model in `skill-store/backend/src/main/java/com/openclaw/skillstore/common/ApiResponse.java`
- [x] T008 [P] Implement global validation error handling in `skill-store/backend/src/main/java/com/openclaw/skillstore/common/GlobalExceptionHandler.java`
- [x] T009 [P] Implement security/CORS/Redis configuration placeholders in `skill-store/backend/src/main/java/com/openclaw/skillstore/config/`
- [x] T010 [P] Define RBAC role enum in `skill-store/backend/src/main/java/com/openclaw/skillstore/auth/StoreRole.java`
- [x] T011 Create Flyway initial schema in `skill-store/backend/src/main/resources/db/migration/V1__init_skill_store_schema.sql`
- [x] T012 [P] Configure backend test profile in `skill-store/backend/src/test/resources/application-test.yml`
- [x] T013 [P] Configure frontend Vite/Vitest/TypeScript files in `skill-store/frontend/vite.config.ts` and `skill-store/frontend/tsconfig.json`

**Checkpoint**: Foundation ready; user story work can be validated independently.

---

## Phase 3: User Story 1 - 识别商店应用入口与边界 (Priority: P1) MVP

**Goal**: 新开发者可以从 README、目录和接口识别商城应用与 Harness 的隔离边界。

**Independent Test**: 阅读根 README、`skill-store/README.md` 和目录结构，10 分钟内能说明
商城前后端边界、模块边界、测试入口和与 Harness 的关系。

### Tests for User Story 1

- [x] T014 [P] [US1] Add Spring context test in `skill-store/backend/src/test/java/com/openclaw/skillstore/SkillStoreApplicationTests.java`
- [x] T015 [P] [US1] Add health API test in `skill-store/backend/src/test/java/com/openclaw/skillstore/api/StoreHealthControllerTest.java`
- [x] T016 [P] [US1] Add frontend route existence test in `skill-store/frontend/tests/router.spec.ts`

### Implementation for User Story 1

- [x] T017 [US1] Implement health API in `skill-store/backend/src/main/java/com/openclaw/skillstore/api/StoreHealthController.java`
- [x] T018 [US1] Implement Vue application shell in `skill-store/frontend/src/app/App.vue` and `skill-store/frontend/src/layouts/StoreShell.vue`
- [x] T019 [US1] Implement three role route registry in `skill-store/frontend/src/router/routes.ts`
- [x] T020 [US1] Document application boundary in `README.md` and `skill-store/README.md`

**Checkpoint**: US1 complete and independently testable.

---

## Phase 4: User Story 2 - 支撑核心业务模块扩展 (Priority: P2)

**Goal**: 买家、创作者、平台运营和公共能力都有明确代码位置、文档说明和示例接口。

**Independent Test**: 检查后端包结构、前端页面目录、模块目录 API 和中文文档，确认关键
后续能力都有归属。

### Tests for User Story 2

- [x] T021 [P] [US2] Add module catalog unit test in `skill-store/backend/src/test/java/com/openclaw/skillstore/modules/StoreModuleCatalogTest.java`
- [x] T022 [P] [US2] Add module API integration test in `skill-store/backend/src/test/java/com/openclaw/skillstore/api/StoreModuleControllerTest.java`
- [x] T023 [P] [US2] Add frontend role page render test in `skill-store/frontend/tests/pages.spec.ts`

### Implementation for User Story 2

- [x] T024 [US2] Implement module descriptors in `skill-store/backend/src/main/java/com/openclaw/skillstore/modules/`
- [x] T025 [US2] Implement module API in `skill-store/backend/src/main/java/com/openclaw/skillstore/api/StoreModuleController.java`
- [x] T026 [P] [US2] Create buyer/creator/admin module placeholders in `skill-store/backend/src/main/java/com/openclaw/skillstore/buyer/`, `creator/`, and `admin/`
- [x] T027 [P] [US2] Create order/storage/audit/notification placeholders in `skill-store/backend/src/main/java/com/openclaw/skillstore/order/`, `storage/`, `audit/`, and `notification/`
- [x] T028 [US2] Implement Skill catalog skeleton in `skill-store/backend/src/main/java/com/openclaw/skillstore/skill/`
- [x] T029 [US2] Implement buyer/creator/admin Vue pages in `skill-store/frontend/src/pages/`
- [x] T030 [US2] Document architecture, API, database, security and deployment in `skill-store/docs/`

**Checkpoint**: US1 and US2 complete and independently testable.

---

## Phase 5: User Story 3 - 准备本地验证与质量门禁 (Priority: P3)

**Goal**: 商城骨架包含可执行验证命令、依赖审计、构建流程、Docker 占位和 README 记录。

**Independent Test**: 按 quickstart 执行后端 verify、前端 test/build/audit，并确认 README
开发记录和 GitHub 提交状态。

### Tests for User Story 3

- [x] T031 [P] [US3] Add validation error test in `skill-store/backend/src/test/java/com/openclaw/skillstore/api/StoreExceptionHandlerTest.java`
- [x] T032 [P] [US3] Add Skill catalog API test in `skill-store/backend/src/test/java/com/openclaw/skillstore/api/SkillCatalogControllerTest.java`
- [x] T033 [P] [US3] Add frontend API wrapper test in `skill-store/frontend/tests/api.spec.ts`

### Implementation for User Story 3

- [x] T034 [US3] Implement frontend API wrapper in `skill-store/frontend/src/api/storeApi.ts`
- [x] T035 [US3] Implement Pinia session placeholder in `skill-store/frontend/src/stores/sessionStore.ts`
- [x] T036 [US3] Create Dockerfile placeholders in `skill-store/infra/docker/`
- [x] T037 [US3] Create Docker Compose placeholder in `skill-store/infra/docker-compose.yml`
- [x] T038 [US3] Create smoke script placeholder in `skill-store/infra/scripts/store-smoke.ps1`
- [x] T039 [US3] Update root README development log and local validation commands in `README.md`
- [x] T040 [US3] Commit and push skeleton implementation to GitHub with message `[skill-store] 完成：前后端工程骨架初始化`

**Checkpoint**: All user stories complete for the skeleton scope.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: 对已实现骨架做最终验证、审计和 Spec 产物追溯补齐。

- [x] T041 [P] Run backend verification with `mvn verify` in `skill-store/backend`
- [x] T042 [P] Run frontend tests with `npm test` in `skill-store/frontend`
- [x] T043 [P] Run frontend production build with `npm run build` in `skill-store/frontend`
- [x] T044 [P] Run high/critical dependency audit with `npm audit --audit-level=high` in `skill-store/frontend`
- [x] T045 [P] Run secret/real-address pattern check across `README.md`, `skill-store/`, and `.specify/`
- [x] T046 Remove generated TypeScript build cache from Git and ignore `*.tsbuildinfo` in `.gitignore`
- [x] T047 Backfill Spec Kit artifacts in `specs/002-init-skill-store/plan.md`, `research.md`, `data-model.md`, `contracts/openapi.yaml`, `quickstart.md`, and `tasks.md`
- [x] T048 Commit and push this Spec Kit artifact backfill to GitHub using `[skill-store] 更新：补齐工程骨架计划任务产物`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: no dependencies.
- **Foundational (Phase 2)**: depends on Setup.
- **US1**: depends on Foundational.
- **US2**: depends on Foundational; can be developed alongside US1 after module API contracts are clear.
- **US3**: depends on Foundational and benefits from US1/US2 test surfaces.
- **Polish**: depends on all selected user stories.

### Parallel Opportunities

- T004/T005, T007-T010/T012/T013, T014-T016, T021-T023, T026/T027/T030,
  T031-T033 and T041-T045 can run in parallel because they touch separate files or are read-only checks.

## Implementation Strategy

### MVP First

1. Complete `skill-store/` directory, backend/frontend dependency files and base configs.
2. Implement US1 health check, Vue shell, routes and README boundary.
3. Validate US1 with backend context/health tests and frontend route tests.

### Incremental Delivery

1. Add US2 module catalog, placeholder packages, role pages and documents.
2. Add US3 validation gates, Docker/Compose placeholders, API wrapper and README records.
3. Run backend verify, frontend test/build/audit and commit each independently verifiable increment.

## Actual Verification Record

- Backend: `mvn verify` passed, 8 tests.
- Frontend: `npm test` passed, 5 tests.
- Frontend build: `npm run build` passed.
- Dependency audit: `npm audit --audit-level=high` found 0 vulnerabilities.
- Security pattern check: no real key or real server address pattern found; package lock integrity hash was excluded as non-secret.
- Implementation commit pushed: `3dae19d [skill-store] 完成：前后端工程骨架初始化`.
