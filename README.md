# CloudKube

A distributed unit conversion application built with Spring Boot microservices and deployed on Kubernetes. This project demonstrates core Kubernetes concepts including deployments, services, ingress, persistent volumes, config maps, secrets, and horizontal pod autoscaling.

---

## Architecture

```
                        ┌─────────────────────────────────────┐
                        │          Kubernetes Cluster         │
                        │                                     │
  Browser               │  ┌─────────────┐                    │
    │                   │  │   Ingress   │                    │
    └──── HTTP ────────────>   (nginx)   │                    │
                        │  └──────┬──────┘                    │
                        │         │                           │
                        │  ┌──────v──────────┐                │
                        │  │ frontend-service│ :8082          │
                        │  │  (Thymeleaf UI) │                │
                        │  └──────┬──────────┘                │
                        │         │                           │
                        │  ┌──────v───────────┐               │
                        │  │conversion-service│ :8080         │
                        │  │  (HPA: 2-5 pods) │               │
                        │  └──────┬───────────┘               │
                        │         │                           │
                        │  ┌──────v──────────┐                │
                        │  │ history-service │ :8081          │
                        │  │   (H2 + PVC)    │                │
                        │  └─────────────────┘                │
                        └─────────────────────────────────────┘
```

### Services

| Service | Port | Role | Technology |
|---|---|---|---|
| `frontend-service` | 8082 | Web UI | Spring Boot + Thymeleaf |
| `conversion-service` | 8080 | Conversion logic | Spring Boot REST API |
| `history-service` | 8081 | Persistence | Spring Boot + H2 + JPA |

### Supported Conversions

| Category | Units |
|---|---|
| Temperature | Celsius, Fahrenheit, Kelvin |
| Distance | Kilometers, Miles, Meters |
| Weight | Kilograms, Pounds, Grams |

---

## Prerequisites

| Tool | Version |
|---|---|
| Docker Desktop | Latest |
| Minikube | v1.38+ |
| kubectl | Latest |
| Java | 17 |

---

## Getting Started

### 1. Run the setup script

Open **PowerShell as Administrator** and run:

```powershell
.\scripts\setup.ps1
```

This will:
- Start Minikube with the Docker driver
- Enable the Ingress and Metrics Server addons
- Build Maven projects and Docker images
- Deploy all Kubernetes manifests
- Configure the hosts file

### 2. Start the tunnel

Open a **second Administrator terminal** and run:

```powershell
minikube tunnel
```

Keep this terminal open.

### 3. Open the application

```
http://unit-converter.local
```

---

## Scripts

| Script | Description |
|---|---|
| `.\scripts\setup.ps1` | Full setup — run this on a fresh clone |
| `.\scripts\setup.ps1 -SkipBuild` | Skip Maven/Docker build (images already exist) |
| `.\scripts\setup.ps1 -SkipHosts` | Skip hosts file modification |
| `.\scripts\start.ps1` | Start the cluster (already set up) |
| `.\scripts\stop.ps1` | Stop and clean up the cluster |
| `.\scripts\status.ps1` | Show pods, services, ingress, and HPA status |

---

## Kubernetes Resources

| File | Kind | Description |
|---|---|---|
| `k8s/configmap.yaml` | ConfigMap | Service URLs injected as environment variables |
| `k8s/secret.yaml` | Secret | H2 credentials (base64 encoded) |
| `k8s/conversion-deployment.yaml` | Deployment + Service | conversion-service, 2 replicas |
| `k8s/history-deployment.yaml` | Deployment + Service + PVC | history-service with persistent storage |
| `k8s/frontend-deployment.yaml` | Deployment + Service | frontend-service, 2 replicas |
| `k8s/ingress.yaml` | Ingress | External access via unit-converter.local |
| `k8s/hpa.yaml` | HorizontalPodAutoscaler | Auto-scales conversion-service (2-5 pods, CPU > 70%) |

---

## Useful Commands

```powershell
# Check pod status
kubectl get pods

# Watch HPA in real time
kubectl get hpa -w

# View logs for a service
kubectl logs -l app=conversion-service

# Manually scale a deployment
kubectl scale deployment conversion-service --replicas=4

# Describe a pod (useful for debugging)
kubectl describe pod <pod-name>

# Delete all resources
kubectl delete -f k8s/
```

---

## Project Structure

```
unit-converter/
├── conversion-service/         # Conversion logic microservice
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── history-service/            # History persistence microservice
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend-service/           # Web UI microservice
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── k8s/                        # Kubernetes manifests
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── conversion-deployment.yaml
│   ├── history-deployment.yaml
│   ├── frontend-deployment.yaml
│   ├── ingress.yaml
│   └── hpa.yaml
├── scripts/                    # Automation scripts
│   ├── setup.ps1
│   ├── start.ps1
│   ├── stop.ps1
│   └── status.ps1
└── docker-compose.yml          # Local development (without Kubernetes)
```

---

## Local Development (without Kubernetes)

To run the application locally using Docker Compose:

```powershell
# Build the JARs
cd conversion-service; .\mvnw.cmd clean package -DskipTests; cd ..
cd history-service;    .\mvnw.cmd clean package -DskipTests; cd ..
cd frontend-service;   .\mvnw.cmd clean package -DskipTests; cd ..

# Start all services
docker-compose up --build
```

Then open `http://localhost:8082` in your browser.

---

## License

MIT
