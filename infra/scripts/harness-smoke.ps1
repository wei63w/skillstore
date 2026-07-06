param(
    [string]$BaseUrl = "http://localhost:8080"
)

$health = Invoke-RestMethod -Uri "$BaseUrl/api/harness/health" -Method Get
if ($health.data.status -ne "UP") {
    throw "Harness health check failed"
}

Write-Output "Harness smoke check passed"
