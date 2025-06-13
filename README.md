# CustomerRegistry

## 🧾 Overview
CustomerRegistry is a Kubernetes-based CRUD application to manage customer data. Built with Kotlin, Ktor, and PostgreSQL, it includes a type-safe client for consumption by external systems and a dedicated integration test suite.

## 📁 Project Structure
```declarative
CustomerRegistry/
├── app/                    # Main Ktor application (API + DB layer)
├── k8s/                    # Kubernetes manifests for app and postgres.
├── common/                 # Shared models, DTOs, and utilities
├── customer-api-client/    # Type-safe HTTP client for Customer API and Integration test suite
```

## ▶️ Run Locally (via Minikube + Skaffold)
Prerequisites:
* Docker/Colima
* Kubernetes
* Minikube
* Skaffold

### Start Minikube
```
minikube start
```

### Run with Skaffold
```
skaffold dev
```
This will build all images, deploy the PostgreSQL DB, and start the customer-app. You can execute different requests using CURL or POSTMAN.

## 🧪 Running Integration Tests
See integration-tests/README.md for details.