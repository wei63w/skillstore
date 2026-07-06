param(
    [string]$BaseUrl = "http://localhost:8081"
)

$health = Invoke-RestMethod -Uri "$BaseUrl/api/store/health" -Method Get
if ($health.data.status -ne "UP") {
    throw "Skill Store health check failed"
}

Write-Output "Skill Store smoke check passed"
