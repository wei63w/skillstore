$required = @("README.md", "harness/README.md", "harness/backend/docs/runtime-artifacts.md")
foreach ($file in $required) {
    if (-not (Test-Path -LiteralPath $file)) {
        throw "Missing required document: $file"
    }
}
Write-Output "Harness documentation boundary check passed"
