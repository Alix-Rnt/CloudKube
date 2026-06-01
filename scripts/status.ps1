<#
.SYNOPSIS
    Shows the current status of the Unit Converter cluster.
#>

Write-Host "`n=== Minikube ===" -ForegroundColor Cyan
minikube status

Write-Host "`n=== Pods ===" -ForegroundColor Cyan
kubectl get pods

Write-Host "`n=== Services ===" -ForegroundColor Cyan
kubectl get services

Write-Host "`n=== Ingress ===" -ForegroundColor Cyan
kubectl get ingress

Write-Host "`n=== HPA ===" -ForegroundColor Cyan
kubectl get hpa