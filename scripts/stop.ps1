<#
.SYNOPSIS
    Stops the Unit Converter cluster.
#>

Write-Host "`nDeleting Kubernetes resources..." -ForegroundColor Cyan
kubectl delete -f (Join-Path (Split-Path -Parent $PSScriptRoot) "k8s") | Out-Null

Write-Host "Stopping Minikube..." -ForegroundColor Cyan
minikube stop

Write-Host "Cluster stopped." -ForegroundColor Green