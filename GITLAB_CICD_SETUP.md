# GitLab CI/CD Setup Guide for Movie Trailer Application

This guide will help you set up GitLab CI/CD pipeline with GitLab Runner for automated build, test, and deployment.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [GitLab Runner Installation](#gitlab-runner-installation)
3. [Runner Registration](#runner-registration)
4. [CI/CD Variables Configuration](#cicd-variables-configuration)
5. [Pipeline Stages](#pipeline-stages)
6. [Deployment Setup](#deployment-setup)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

- GitLab account (GitLab.com or self-hosted)
- Server with Docker installed (for GitLab Runner)
- PostgreSQL database
- SSH access to deployment servers (for production deployment)

---

## GitLab Runner Installation

### Option 1: Docker-based Runner (Recommended)

```bash
# Pull GitLab Runner image
docker pull gitlab/gitlab-runner:latest

# Run GitLab Runner container
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest
```

### Option 2: Native Installation (Linux)

```bash
# Add GitLab Runner repository
curl -L "https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh" | sudo bash

# Install GitLab Runner
sudo apt-get install gitlab-runner

# Start the runner
sudo gitlab-runner start
```

### Option 3: macOS Installation

```bash
# Download and install
sudo curl --output /usr/local/bin/gitlab-runner "https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-darwin-amd64"
sudo chmod +x /usr/local/bin/gitlab-runner

# Install and start
cd ~
gitlab-runner install
gitlab-runner start
```

---

## Runner Registration

### 1. Get Registration Token

Navigate to your GitLab project:

```
Settings → CI/CD → Runners → Expand
```

Copy the registration token.

### 2. Register the Runner

```bash
# Interactive registration
sudo gitlab-runner register

# Or with Docker
docker exec -it gitlab-runner gitlab-runner register
```

You'll be prompted for:

- **GitLab instance URL**: `https://gitlab.com/` (or your GitLab URL)
- **Registration token**: (paste your token)
- **Description**: `movie-trailer-runner`
- **Tags**: `docker` (important for job matching)
- **Executor**: `docker`
- **Default Docker image**: `maven:3.9-amazoncorretto-25`

### 3. Verify Runner

Check if runner is active:

```bash
sudo gitlab-runner verify
```

Or in GitLab UI:

```
Settings → CI/CD → Runners
```

---

## CI/CD Variables Configuration

Navigate to: `Settings → CI/CD → Variables`

### Required Variables

#### Docker Registry (for Jib/Docker builds)

```
CI_REGISTRY = registry.gitlab.com
CI_REGISTRY_USER = <your-gitlab-username>
CI_REGISTRY_PASSWORD = <your-gitlab-access-token>
CI_REGISTRY_IMAGE = registry.gitlab.com/<username>/movie-trailer
```

#### SSH Deployment Variables

```
SSH_PRIVATE_KEY = <your-ssh-private-key>
STAGING_SERVER = staging.yourdomain.com
STAGING_USER = deploy
PRODUCTION_SERVER = prod.yourdomain.com
PRODUCTION_USER = deploy
```

#### Kubernetes Variables (if using K8s)

```
KUBE_URL = https://kubernetes.yourdomain.com
KUBE_TOKEN = <kubernetes-service-account-token>
KUBE_NAMESPACE = movie-trailer-prod
```

#### Database Variables (for integration tests)

```
POSTGRES_DB = movie_trailers_test
POSTGRES_USER = testuser
POSTGRES_PASSWORD = testpass
```

### Variable Security

- Mark sensitive variables as **Protected** and **Masked**
- Use **Environment-specific variables** for staging/production
- Enable **Protected** flag for production variables

---

## Pipeline Stages

The CI/CD pipeline consists of 4 main stages:

### 1. Build Stage

```yaml
- Compiles Java source code
- Uses Maven with Amazon Corretto 25
- Caches dependencies
```

### 2. Test Stage

```yaml
- Unit Tests: Runs JUnit tests
- Integration Tests: Uses PostgreSQL service
- Code Quality: Checkstyle analysis
```

### 3. Package Stage

```yaml
- JAR Packaging: Creates executable JAR
- Docker Image: Builds with Jib Maven plugin
- Pushes to GitLab Container Registry
```

### 4. Deploy Stage

```yaml
- Staging: Auto-deploy to staging (manual trigger)
- Production: Deploy to production (manual trigger)
- Kubernetes: K8s deployment (optional)
```

---

## Deployment Setup

### Method 1: Docker Compose Deployment

#### On Target Server:

1. **Install Docker and Docker Compose**

```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo apt-get install docker-compose-plugin
```

2. **Create deployment directory**

```bash
sudo mkdir -p /opt/movie-trailer
cd /opt/movie-trailer
```

3. **Copy production files**

```bash
# Copy docker-compose.prod.yml
sudo vim docker-compose.yml

# Create .env file
sudo vim .env
```

4. **Configure environment**

```bash
# Edit .env with production values
POSTGRES_PASSWORD=your_secure_password
CI_REGISTRY_IMAGE=registry.gitlab.com/username/movie-trailer
IMAGE_TAG=latest
```

5. **Login to GitLab Registry**

```bash
docker login registry.gitlab.com
```

6. **Deploy**

```bash
docker-compose up -d
```

### Method 2: Kubernetes Deployment

#### Create Kubernetes Manifests:

**deployment.yaml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: movie-trailer
  namespace: movie-trailer-prod
spec:
  replicas: 3
  selector:
    matchLabels:
      app: movie-trailer
  template:
    metadata:
      labels:
        app: movie-trailer
    spec:
      containers:
        - name: movie-trailer
          image: registry.gitlab.com/username/movie-trailer:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: SPRING_DATASOURCE_URL
              valueFrom:
                secretKeyRef:
                  name: movie-trailer-secrets
                  key: database-url
```

**service.yaml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: movie-trailer-service
  namespace: movie-trailer-prod
spec:
  selector:
    app: movie-trailer
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

#### Apply manifests:

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

---

## Running the Pipeline

### Automatic Triggers

- **Push to any branch**: Runs build and test stages
- **Push to main/develop**: Runs full pipeline including package
- **Create tag**: Triggers production build

### Manual Triggers

- **Staging Deployment**: Click "Play" button in pipeline
- **Production Deployment**: Click "Play" button (requires approval)
- **Rollback**: Use rollback job if needed

### Example Workflow

```bash
# 1. Create feature branch
git checkout -b feature/new-feature

# 2. Make changes and commit
git add .
git commit -m "Add new feature"

# 3. Push to GitLab (triggers CI)
git push origin feature/new-feature

# 4. Merge to develop (triggers staging)
# After review, merge MR

# 5. Deploy to production
# Create tag for release
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# Manual trigger production deployment in GitLab UI
```

---

## Monitoring Pipeline

### View Pipeline Status

```
CI/CD → Pipelines → Select pipeline
```

### View Job Logs

```
Click on any job → View full logs
```

### Download Artifacts

```
Job page → Browse button → Download artifacts
```

---

## Troubleshooting

### Common Issues

#### 1. Runner Not Picking Up Jobs

**Problem**: Jobs stuck in "pending"

**Solution**:

```bash
# Check runner status
sudo gitlab-runner verify

# Restart runner
sudo gitlab-runner restart

# Check tags match
# Job tags must match runner tags (e.g., "docker")
```

#### 2. Docker Permission Denied

**Problem**: "permission denied while trying to connect to Docker daemon"

**Solution**:

```bash
# Add gitlab-runner to docker group
sudo usermod -aG docker gitlab-runner

# Restart runner
sudo gitlab-runner restart
```

#### 3. Maven Build Fails

**Problem**: Dependencies not downloading

**Solution**:

```bash
# Clear Maven cache in GitLab
# CI/CD → Pipelines → Clear runner caches

# Or add to .gitlab-ci.yml:
cache:
  key: ${CI_COMMIT_REF_SLUG}
  policy: pull-push
```

#### 4. Docker Image Push Fails

**Problem**: "unauthorized: authentication required"

**Solution**:

```bash
# Verify CI/CD variables:
CI_REGISTRY_USER = your-gitlab-username
CI_REGISTRY_PASSWORD = personal-access-token (not password)

# Generate personal access token:
# GitLab → Settings → Access Tokens → Create token with "read_registry" and "write_registry" scopes
```

#### 5. PostgreSQL Connection Issues

**Problem**: Integration tests fail with connection error

**Solution**:

```yaml
# In .gitlab-ci.yml, ensure service alias matches connection URL:
services:
  - name: postgres:18.0-alpine3.22
    alias: postgres  # Important!

variables:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/test_db
```

---

## Advanced Configuration

### Custom Runner Configuration

Edit `/etc/gitlab-runner/config.toml`:

```toml
concurrent = 4
check_interval = 0

[[runners]]
name = "movie-trailer-runner"
url = "https://gitlab.com/"
token = "YOUR_TOKEN"
executor = "docker"
[runners.docker]
tls_verify = false
image = "maven:3.9-amazoncorretto-25"
privileged = true
disable_cache = false
volumes = ["/cache", "/var/run/docker.sock:/var/run/docker.sock"]
shm_size = 0
[runners.cache]
[runners.cache.s3]
[runners.cache.gcs]
```

### Pipeline Optimization

1. **Use Pipeline DAG** for parallel execution
2. **Enable Docker layer caching**
3. **Use artifact dependencies** strategically
4. **Implement pipeline rules** for conditional execution

### Security Best Practices

1. **Scan Docker images** for vulnerabilities
2. **Use signed commits**
3. **Enable branch protection**
4. **Require approval** for production deployments
5. **Rotate access tokens** regularly

---

## Performance Optimization

### Maven Build Speed

```yaml
variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"

cache:
  paths:
    - .m2/repository/
```

### Docker Build Speed

```yaml
# Use BuildKit
variables:
  DOCKER_BUILDKIT: 1
```

---

## Useful Commands

```bash
# View runner status
sudo gitlab-runner status

# View runner logs
sudo gitlab-runner --debug run

# Unregister runner
sudo gitlab-runner unregister --name movie-trailer-runner

# Update runner
sudo gitlab-runner stop
sudo apt-get update && sudo apt-get install gitlab-runner
sudo gitlab-runner start

# Clear local Docker images
docker system prune -a -f
```

---

## Additional Resources

- [GitLab CI/CD Documentation](https://docs.gitlab.com/ee/ci/)
- [GitLab Runner Documentation](https://docs.gitlab.com/runner/)
- [Docker Executor](https://docs.gitlab.com/runner/executors/docker.html)
- [Pipeline Configuration Reference](https://docs.gitlab.com/ee/ci/yaml/)

---

## Support

For issues or questions:

1. Check GitLab Runner logs
2. Review pipeline job logs
3. Check this documentation
4. Contact DevOps team

---

**Last Updated**: 2025-10-29
**Version**: 1.0.0
