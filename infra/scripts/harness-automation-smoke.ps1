param(
    [string]$RepoRoot = (Resolve-Path "$PSScriptRoot\..\..").Path
)

$ErrorActionPreference = "Stop"

Write-Host "Checking Harness automation planning artifacts..."
$required = @(
    "specs/004-harness-automation/spec.md",
    "specs/004-harness-automation/plan.md",
    "specs/004-harness-automation/tasks.md",
    "specs/004-harness-automation/contracts/openapi.yaml",
    "specs/004-harness-automation/contracts/cli-contract.md"
)

foreach ($path in $required) {
    $fullPath = Join-Path $RepoRoot $path
    if (-not (Test-Path $fullPath)) {
        throw "Missing required artifact: $path"
    }
}

Write-Host "Harness automation planning artifacts are present."
