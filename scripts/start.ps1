<#
.SYNOPSIS
    Starts the Unit Converter cluster (already set up).
.NOTES
    Run setup.ps1 first if this is a fresh clone.
#>

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

Write-Host "`nStarting Minikube..." -ForegroundColor Cyan
minikube start --driver=docker

Write-Host "Waiting for pods..." -ForegroundColor Cyan
kubectl wait --for=condition=ready pod -l app=history-service --timeout=120s | Out-Null
kubectl wait --for=condition=ready pod -l app=conversion-service --timeout=120s | Out-Null
kubectl wait --for=condition=ready pod -l app=frontend-service --timeout=120s | Out-Null

Write-Host "`nCluster is up." -ForegroundColor Green
Write-Host "  Run 'minikube tunnel' in an Administrator terminal." -ForegroundColor Yellow
Write-Host "  Then open http://unit-converter.local" -ForegroundColor Cyan