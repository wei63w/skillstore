# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]

**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit-plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

**Documentation Language**: All generated project documents default to Chinese.
Use English only for code identifiers, third-party protocol names, API fields, or
when the feature explicitly requires it.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: [e.g., Python 3.11, Swift 5.9, Rust 1.75 or NEEDS CLARIFICATION]

**Primary Dependencies**: [e.g., FastAPI, UIKit, LLVM or NEEDS CLARIFICATION]

**Storage**: [if applicable, e.g., PostgreSQL, CoreData, files or N/A]

**Testing**: [e.g., pytest, XCTest, cargo test or NEEDS CLARIFICATION]

**Target Platform**: [e.g., Linux server, iOS 15+, WASM or NEEDS CLARIFICATION]

**Project Type**: [e.g., library/cli/web-service/mobile-app/compiler/desktop-app or NEEDS CLARIFICATION]

**Performance Goals**: [domain-specific, e.g., 1000 req/s, 10k lines/sec, 60 fps or NEEDS CLARIFICATION]

**Constraints**: [domain-specific, e.g., <200ms p95, <100MB memory, offline-capable or NEEDS CLARIFICATION]

**Scale/Scope**: [domain-specific, e.g., 10k users, 1M LOC, 50 screens or NEEDS CLARIFICATION]

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Document how this plan satisfies the active constitution. Any violation MUST be
listed in Complexity Tracking with a justification, rejected simpler alternative,
owner, and remediation date.

- **Observability & Reproducibility**: Persistent logs, checkpoints, artifacts,
  rebuild inputs, and replay/resume strategy are defined.
- **Human Risk Gates**: Ambiguous requirements, high-risk infrastructure actions,
  permission/credential/payment/privacy/security changes, and unrecoverable
  release blockers have structured confirmation points.
- **Harness/Business Decoupling**: Agent Dev Harness modules remain reusable and
  independent from OpenClaw Skill Store business rules.
- **Security Shift-Left**: Input validation, RBAC/authentication, upload checks,
  secret handling, SAST, dependency scanning, and agent sandboxing are planned.
- **Automation Loop**: Requirements, architecture, data model, API contracts,
  implementation, tests, scans, formatting, container build, deployment, smoke
  validation, and report generation are represented.
- **Testing Gates**: Unit, integration, E2E where applicable, coverage reporting,
  and three-round auto-repair behavior are defined; coverage below 80% blocks
  build/deployment.
- **Performance Baselines**: Harness and Skill Store latency, concurrency,
  recovery, cache, file distribution, and first-screen targets are stated or
  explicitly marked not applicable.
- **Deployment & Rollback**: Docker, environment isolation, Nginx/HTTPS for demo,
  smoke checks, persistent logs, and rollback behavior are specified.
- **Documentation**: Requirements, architecture, ER/data model, API contracts,
  test report, deployment runbook, and Mermaid diagrams are planned.
- **README & GitHub Records**: README.md update scope is identified, and each
  independently verifiable small feature/fix has a planned GitHub commit with
  commit message following `[module] action: content`.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
# [REMOVE IF UNUSED] Option 1: Single project (DEFAULT)
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [REMOVE IF UNUSED] Option 2: Web application (when "frontend" + "backend" detected)
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [REMOVE IF UNUSED] Option 3: Mobile + API (when "iOS/Android" detected)
api/
└── [same as backend above]

ios/ or android/
└── [platform-specific structure: feature modules, UI flows, platform tests]
```

**Structure Decision**: [Document the selected structure and reference the real
directories captured above]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
