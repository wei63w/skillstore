# Harness Workflow Requirements Checklist: 完善 Agent Dev Harness 自主开发能力

**Purpose**: Validate Harness automation requirements quality before planning
**Created**: 2026-07-07
**Feature**: [spec.md](../spec.md)
**Focus Areas**: Spec Kit 流水线编排、代码/测试执行器、可观测性、失败恢复、GitHub 交付证据
**Depth**: Standard release-gate review
**Audience**: Reviewer before `/speckit-plan`

## Requirement Completeness

- [x] CHK001 Are the mandatory Spec Kit stages specified in strict order before implementation? [Completeness, Spec §FR-002, Spec §CR-010]
- [x] CHK002 Are requirements defined for creating or locating a unique development task from a business goal? [Completeness, Spec §FR-001]
- [x] CHK003 Are requirements defined for driving implementation from `tasks.md` task state, dependency order, and parallel markers? [Completeness, Spec §FR-005]
- [x] CHK004 Are requirements defined for both code generation and documentation update work, not only source code changes? [Completeness, Spec §FR-006]
- [x] CHK005 Are requirements defined for test execution across unit, integration, frontend, build, security, and dependency checks? [Completeness, Spec §FR-007]
- [x] CHK006 Are requirements defined for final reports covering stages, artifacts, code changes, tests, scans, retries, human confirmations, and residual risk? [Completeness, Spec §FR-010]

## Requirement Clarity

- [x] CHK007 Is the retry limit quantified and consistently stated as at most three automatic repair attempts? [Clarity, Spec §FR-008, Spec §SC-004]
- [x] CHK008 Is the recovery target quantified with a 30-second restore expectation? [Clarity, Spec §SC-003]
- [x] CHK009 Is the boundary between Harness core and OpenClaw Skill Store business rules explicitly defined? [Clarity, Spec §FR-014, Spec §SC-008]
- [x] CHK010 Is the meaning of high-risk operations sufficiently defined through examples such as credentials, payment, privacy, security, ports, and deployment? [Clarity, Spec §Edge Cases, Spec §CR-002]
- [x] CHK011 Are GitHub submission outcomes defined for success and remote push failure? [Clarity, Spec §US4, Spec §FR-012]

## Requirement Consistency

- [x] CHK012 Are README development-log requirements consistent between functional requirements and constitution requirements? [Consistency, Spec §FR-011, Spec §CR-008]
- [x] CHK013 Are human confirmation requirements consistent across ambiguity, high-risk operations, and three-round failure exhaustion? [Consistency, Spec §FR-009, Spec §Edge Cases]
- [x] CHK014 Are observability requirements consistent across phase records, tool calls, artifacts, retries, and final reports? [Consistency, Spec §FR-003, Spec §FR-010, Spec §FR-015]
- [x] CHK015 Are success criteria aligned with the user stories rather than introducing unrelated capabilities? [Consistency, Spec §User Scenarios, Spec §Success Criteria]

## Scenario Coverage

- [x] CHK016 Are primary success scenarios covered for workflow orchestration, implementation execution, reporting, and GitHub evidence? [Coverage, Spec §US1..US4]
- [x] CHK017 Are exception scenarios covered for missing artifacts, failed checks, process interruption, dirty worktree, and target spec changes? [Coverage, Spec §Edge Cases]
- [x] CHK018 Are recovery scenarios covered for interrupted pipelines and failed auto-repair attempts? [Coverage, Spec §US1, Spec §US2, Spec §Edge Cases]
- [x] CHK019 Are security-sensitive scenarios covered for secrets, real payment configuration, permissions, privacy, and deployment risk gates? [Coverage, Spec §FR-013, Spec §CR-002]

## Acceptance Criteria Quality

- [x] CHK020 Can each user story be independently tested without requiring a full production deployment? [Measurability, Spec §Independent Test]
- [x] CHK021 Are completion signals measurable for stage records, recovery time, retry count, confirmation gates, README/GitHub records, and business-code separation? [Measurability, Spec §SC-001..SC-008]
- [x] CHK022 Are acceptance scenarios written in Given/When/Then form with observable outcomes? [Acceptance Criteria, Spec §Acceptance Scenarios]

## Dependencies & Assumptions

- [x] CHK023 Are assumptions documented for prioritizing交付物 A, using Skill Store as validation target, and avoiding real destructive cloud operations? [Assumption, Spec §Assumptions]
- [x] CHK024 Are staged implementation assumptions documented for starting with controlled templates/task context/local tool calls before more complex multi-agent routing? [Assumption, Spec §Assumptions]
- [x] CHK025 Are GitHub availability and blocked-submit behavior documented? [Dependency, Spec §Assumptions, Spec §US4]

## Ambiguities & Conflicts

- [x] CHK026 Does the spec avoid unresolved `[NEEDS CLARIFICATION]` markers while retaining the clarify session outcome? [Ambiguity, Spec §Clarifications]
- [x] CHK027 Are no requirements in the spec in conflict with the constitution requirement that checklist precedes plan? [Conflict, Spec §Workflow Gate]
