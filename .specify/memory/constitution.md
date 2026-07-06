<!--
Sync Impact Report
Version change: template -> 1.0.0
Modified principles:
- template principle 1 -> I. Observability and Reproducible Delivery First
- template principle 2 -> II. Minimal Human Intervention with Explicit Risk Gates
- template principle 3 -> III. Layered Decoupling Between Harness and Business Apps
- template principle 4 -> IV. Security Shift-Left and Sandboxed Agent Operations
- template principle 5 -> V. Standardized Autonomous Automation Loop
Added sections:
- Platform Architecture and Delivery Scope
- Engineering Standards and Quality Gates
Removed sections:
- None
Templates requiring updates:
- updated: .specify/templates/plan-template.md
- updated: .specify/templates/spec-template.md
- updated: .specify/templates/tasks-template.md
- not present: .specify/templates/commands/*.md
- not present: README.md, docs/quickstart.md, AGENTS.md, CLAUDE.md
Follow-up TODOs:
- None
-->

# AI Autonomous Development System + OpenClaw Skill Store Constitution

## Core Principles

### I. Observability and Reproducible Delivery First

Every autonomous development step MUST be persisted as structured, replayable
evidence: agent actions, inputs, outputs, tool calls, code diffs, test logs,
build records, deployment records, elapsed time, and validation results. Long
running work MUST support interruption recovery, checkpoint resume, failure
retry, and reconstruction of all intermediate artifacts including requirements,
architecture notes, source code, reports, images, and deployment configuration.
Any code, container image, database migration, or environment configuration that
cannot be rebuilt from versioned inputs is non-compliant.

Rationale: the project exists to prove long-running autonomous software delivery;
without durable traces and reproducible outputs, autonomy cannot be trusted,
debugged, audited, or resumed.

### II. Minimal Human Intervention with Explicit Risk Gates

Agents MUST autonomously execute routine requirements analysis, architecture
drafting, CRUD implementation, page rendering, test generation, formatting,
container packaging, and static deployment. Agents MUST block and request human
confirmation only for the following cases: ambiguous business requirements with
materially different valid implementations, high-risk infrastructure changes
such as opening ports or destroying online environments, payment/privacy/security
configuration, permission or credential changes, and release-blocking defects or
performance failures that remain unresolved after the allowed repair loop.
Human-facing prompts MUST be structured with the item requiring confirmation,
risk level, available options, and the consequence of each option.

Rationale: human engineers should guide ambiguous or risky decisions while the AI
system owns the standard delivery flow end to end.

### III. Layered Decoupling Between Harness and Business Apps

The Agent Dev Harness MUST remain a reusable development platform independent of
the OpenClaw Skill Store. The harness may generate, test, package, and deploy the
store, but MUST NOT embed store-specific business rules into core scheduling,
context management, persistence, tool orchestration, or review modules. The
required harness layers are planning agent, coding agent, testing agent,
build/deploy agent, review agent, task-state persistence, context-window
management, iterative self-checking, human-interaction hooks, and a tool plugin
pool. Business application code MUST live in its own frontend, backend, database,
cache, test, and deployment boundaries.

Rationale: the Skill Store is the verification case, not the product boundary of
the autonomous development platform.

### IV. Security Shift-Left and Sandboxed Agent Operations

Security checks MUST run during generation, review, build, and release. Generated
code MUST include input validation, exception handling, unified response shapes,
parameterized queries, authentication checks, RBAC enforcement, protected file
upload handling, and safe frontend request/error handling. The system MUST never
generate or commit plaintext secrets, database addresses, administrator
credentials, real payment card storage, unsafe SQL concatenation, unaudited file
execution, or privilege-escalating agent operations. Skill uploads MUST be
scanned for malicious scripts, high-risk system calls, dependency vulnerabilities,
file type violations, and size violations before listing. Agent operations MUST
run in a permission sandbox, and high-risk operations MUST require the risk gate
defined in Principle II.

Rationale: autonomous coding increases throughput only if security is built into
the earliest possible step and enforced again at every promotion boundary.

### V. Standardized Autonomous Automation Loop

Each feature MUST move through a documented loop: requirements, architecture,
data model, API contract, implementation, unit tests, integration tests, frontend
E2E tests when applicable, SAST and dependency scanning, formatting, container
build, deployment, smoke validation, and report generation. Agents MUST self-check
after each phase and automatically rework failures up to three repair rounds
before requesting human confirmation. Unit coverage below 80%, high-risk security
findings, failed core E2E flows, or unreconciled performance baselines MUST block
packaging and deployment.

Rationale: a repeatable quality gate turns AI output into an auditable delivery
pipeline instead of an unbounded code generation session.

## Platform Architecture and Delivery Scope

The project has three governed deliverables:

1. Agent Dev Harness Demo: layered agent scheduler, long-task persistence,
   checkpoint resume, context compression, plugin tool pool, human confirmation
   hooks, code formatting, test generation, SAST, dependency scanning, Docker
   build, cloud deployment integration, Git integration, system tests, reports,
   and operator documentation.
2. OpenClaw Skill Store Web Demo: buyer frontend, creator console, platform admin
   console, skill file storage and distribution, dependency/version management,
   sandbox permission detection, payment split simulation, notifications, MySQL
   initialization, Redis configuration, automated tests, Docker packaging,
   one-command deployment, and full business documentation.
3. Unified governance and standards: this constitution, feature specifications,
   architecture documents, database ER diagrams, API documents, test reports,
   deployment runbooks, and Mermaid diagrams that evolve with the codebase.

Required baseline stack for the Skill Store is React or Vue 3 on the frontend,
NestJS or Spring Boot on the backend, MySQL 8 for persistence, Redis for cache,
Docker for packaging, and separated development, test, and demo environments.
Source layout MUST separate controllers, services, data access, utilities,
constants, configuration, tests, and deployment assets. Configuration, secrets,
database settings, and environment-specific values MUST live in environment
variables or a configuration center, never in application source.

The Skill Store MUST include buyer registration/login, categorized marketplace
home, vector search, skill detail pages, cart, simulated checkout, purchased
skill download or online trial, order records, favorites, reviews, key
management, creator onboarding, skill package upload, version management, pricing
for free/buyout/subscription, creator analytics, settlement simulation, platform
security review, takedown, category and homepage operations, account management,
order management, and audit logs.

## Engineering Standards and Quality Gates

Naming MUST be semantic and consistent: classes use PascalCase, methods and
variables use camelCase, and database tables and columns use snake_case.
Meaningless abbreviations are forbidden. Public classes, public APIs, and complex
business logic MUST have documentation comments, and critical flows MUST include
short comments explaining the design intent.

Testing is mandatory. Core service-layer methods target 100% unit coverage. All
backend APIs MUST have integration coverage for validation, happy path, and
error responses. Marketplace core flows including browsing, ordering, purchase,
and skill publishing MUST have automated E2E coverage. Test reports MUST include
coverage, executed cases, defect list, and vulnerability severity.

Performance baselines are mandatory unless a feature explicitly documents why a
baseline does not apply. Harness single-module code generation MUST target five
minutes or less, checkpoint recovery MUST target 30 seconds or less, and the
harness MUST support up to three independent concurrent development pipelines.
Skill Store ordinary query APIs MUST target 200 ms or less, complex paginated
lists MUST target 500 ms or less, a single instance MUST support 200 concurrent
users without timeout, first screen render MUST target 1.5 seconds or less, and
large skill packages MUST use bandwidth limiting, resumable download, and
chunked storage where applicable. Homepage categories, popular skills, and skill
details MUST use Redis caching.

Deployment MUST be automated and containerized. Dockerfiles MUST use layered
builds that minimize image size. Deployment automation MUST be able to pull an
image, start containers, configure Nginx reverse proxy, apply HTTPS/SSL for
networked demos, run smoke checks, and roll back to the previous image on
failure. Logs MUST use a unified structured format and persistent storage with
module and severity search.

## Governance

This constitution supersedes conflicting feature plans, task lists, templates,
and implementation habits. Every new specification and implementation plan MUST
include an explicit constitution check covering observability, human risk gates,
layered decoupling, security, tests, performance, deployment reproducibility, and
documentation. Any non-compliance MUST be recorded with a justification,
alternative considered, owner, and remediation date before implementation begins.

Amendments MUST be made by updating this file, recording the impact report at
the top of the file, synchronizing affected Spec Kit templates, and documenting
the version change. Versioning follows semantic versioning: MAJOR for removing
or redefining principles in a way that invalidates existing governance, MINOR
for adding principles or materially expanding required sections, and PATCH for
clarifications that preserve existing obligations.

Compliance review is required at three gates: before Phase 0 research, after
Phase 1 design, and before deployment. Release is blocked by missing required
documentation, missing persistent logs for agent actions, unit coverage below
80%, high-risk security findings, failed core E2E flows, unapproved risk-gate
operations, unreproducible build inputs, or failed deployment rollback checks.

**Version**: 1.0.0 | **Ratified**: 2026-07-06 | **Last Amended**: 2026-07-06
