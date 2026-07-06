$readme = Get-Content -Raw -LiteralPath "README.md"
if ($readme -notmatch "GitHub") {
    throw "README.md is missing GitHub status content"
}
if ($readme -notmatch "Agent Dev Harness") {
    throw "README.md is missing Agent Dev Harness implementation record"
}
if ($readme -notmatch "mvn verify") {
    throw "README.md is missing verification evidence"
}
Write-Output "README development log check passed"
