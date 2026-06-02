<#
.SYNOPSIS
    Full setup script for Unit Converter on Kubernetes.
.DESCRIPTION
    Builds Docker images, deploys to Minikube, and configures the hosts file.
.NOTES
    Must be run as Administrator.
    Requires: Docker Desktop, Minikube, kubectl
#>

param (
    [switch]$SkipBuild,    # Skip Maven build and Docker image creation
    [switch]$SkipHosts     # Skip hosts file modification
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ── Helpers ────────────────────────────────────────────────────────────────────

function Write-Step($n, $total, $msg) {
    Write-Host "`n[$n/$total] $msg" -ForegroundColor Cyan
}

function Write-Success($msg) { Write-Host "$msg" -ForegroundColor Green }
function Write-Warn($msg)    { Write-Host "$msg" -ForegroundColor Yellow }
function Write-Fail($msg)    { Write-Host "$msg" -ForegroundColor Red }

function Assert-Command($cmd) {
    if (-not (Get-Command $cmd -ErrorAction SilentlyContinue)) {
        Write-Fail "$cmd is not installed or not in PATH."
        exit 1
    }
}

# ── Prerequisites ───────────────────────────────────────────────────────────────

Write-Host "╔══════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     Unit Converter — Kubernetes Setup    ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════╝" -ForegroundColor Cyan

Write-Step 0 6 "Checking prerequisites..."
Assert-Command "docker"
Assert-Command "minikube"
Assert-Command "kubectl"
Write-Success "All prerequisites found."

# ── Minikube ────────────────────────────────────────────────────────────────────

Write-Step 1 6 "Starting Minikube..."
$status = minikube status --format="{{.Host}}" 2>$null
if ($status -ne "Running") {
    minikube start --driver=docker --cpus=2 --memory=4096
    Write-Success "Minikube started."
} else {
    Write-Success "Minikube already running."
}

Write-Step 2 6 "Enabling addons..."
minikube addons enable ingress | Out-Null
minikube addons enable metrics-server | Out-Null
Write-Success "Addons enabled: ingress, metrics-server."

# ── Docker images ───────────────────────────────────────────────────────────────

Write-Step 3 6 "Building Docker images..."

if ($SkipBuild) {
    Write-Warn "Skipping build (--SkipBuild flag set)."
} else {
    minikube docker-env | Invoke-Expression

    $services = @("conversion-service", "history-service", "frontend-service")
    $root = Split-Path -Parent $PSScriptRoot

    foreach ($svc in $services) {
        Write-Host "  -> Building $svc..." -ForegroundColor Gray
        $svcPath = Join-Path $root $svc
        Push-Location $svcPath
        cmd /c "mvnw clean package -DskipTests -q"
        docker build -t "${svc}:1.0" . -q
        Pop-Location
        Write-Success "$svc:1.0 built."
    }
}

# ── Kubernetes deployment ────────────────────────────────────────────────────────

Write-Step 4 6 "Deploying to Kubernetes..."
$k8s = Join-Path (Split-Path -Parent $PSScriptRoot) "k8s"

$manifests = @(
    "configmap.yaml",
    "secret.yaml",
    "history-deployment.yaml",
    "conversion-deployment.yaml",
    "frontend-deployment.yaml",
    "ingress.yaml",
    "hpa.yaml"
)

foreach ($manifest in $manifests) {
    kubectl apply -f "$k8s\$manifest" | Out-Null
    Write-Success "$manifest applied."
}

# ── Wait for pods ────────────────────────────────────────────────────────────────

Write-Step 5 6 "Waiting for pods to be ready..."
$labels = @("history-service", "conversion-service", "frontend-service")
foreach ($label in $labels) {
    kubectl wait --for=condition=ready pod -l app=$label --timeout=120s | Out-Null
    Write-Success "$label is ready."
}

# ── Hosts file ───────────────────────────────────────────────────────────────────

Write-Step 6 6 "Configuring hosts file..."

if ($SkipHosts) {
    Write-Warn "Skipping hosts file modification (--SkipHosts flag set)."
} else {
    $hostsFile = "C:\Windows\System32\drivers\etc\hosts"
    $entry     = "127.0.0.1 unit-converter.local"
    $content   = Get-Content $hostsFile -Raw

    if ($content -notmatch "unit-converter\.local") {
        Add-Content -Path $hostsFile -Value "`n$entry"
        Write-Success "Hosts file updated."
    } else {
        Write-Success "Hosts entry already present."
    }
}

# ── Done ─────────────────────────────────────────────────────────────────────────

Write-Host "`n╔══════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║           Setup complete!               ║" -ForegroundColor Green
Write-Host "╚══════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
Write-Host "  Next step:" -ForegroundColor White
Write-Host "  Open a new Administrator terminal and run:" -ForegroundColor White
Write-Host "    minikube tunnel" -ForegroundColor Yellow
Write-Host ""
Write-Host "  Then open your browser at:" -ForegroundColor White
Write-Host "    http://unit-converter.local" -ForegroundColor Cyan
Write-Host ""