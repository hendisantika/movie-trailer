# GitLab CI/CD Quick Start Guide

## 5-Minute Setup

### Step 1: Push Code to GitLab

```bash
git remote add gitlab https://gitlab.com/yourusername/movie-trailer.git
git push -u gitlab main
```

### Step 2: Install GitLab Runner (Docker)

```bash
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest
```

### Step 3: Register Runner

```bash
docker exec -it gitlab-runner gitlab-runner register
```

**Inputs:**

- URL: `https://gitlab.com/`
- Token: (from GitLab Settings → CI/CD → Runners)
- Description: `movie-trailer-runner`
- Tags: `docker`
- Executor: `docker`
- Image: `maven:3.9-amazoncorretto-25`

### Step 4: Add CI/CD Variables

Navigate to: **Settings → CI/CD → Variables**

Add these variables:

```
CI_REGISTRY_USER = your-gitlab-username
CI_REGISTRY_PASSWORD = your-personal-access-token
```

To create Personal Access Token:

1. GitLab → User Settings → Access Tokens
2. Create token with scopes: `read_registry`, `write_registry`
3. Copy token value

### Step 5: Trigger Pipeline

```bash
# Make any change and push
git add .
git commit -m "Trigger CI/CD pipeline"
git push gitlab main
```

### Step 6: Monitor Pipeline

Go to: **CI/CD → Pipelines**

---

## Common Commands

### View Pipeline Status

```bash
# In GitLab UI
CI/CD → Pipelines → Latest pipeline
```

### Manual Deployment

```bash
# In pipeline view, click "Play" button on:
- deploy:staging (for staging)
- deploy:production (for production)
```

### Build Docker Image Locally

```bash
# Test Dockerfile
docker build -t movie-trailer:test .

# Run locally
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/movie_trailers \
  -e SPRING_DATASOURCE_USERNAME=movieuser \
  -e SPRING_DATASOURCE_PASSWORD=yourpass \
  movie-trailer:test
```

### Deploy with Docker Compose

```bash
# Copy .env.example to .env
cp .env.example .env

# Edit .env with your values
vim .env

# Deploy
docker-compose -f docker-compose.prod.yml up -d

# View logs
docker-compose -f docker-compose.prod.yml logs -f

# Stop
docker-compose -f docker-compose.prod.yml down
```

---

## Pipeline Stages Overview

| Stage       | Jobs                                          | Description                 |
|-------------|-----------------------------------------------|-----------------------------|
| **Build**   | build                                         | Compile Java code           |
| **Test**    | test:unit<br>test:integration<br>code:quality | Run tests and checks        |
| **Package** | package:jar<br>package:docker                 | Create JAR and Docker image |
| **Deploy**  | deploy:staging<br>deploy:production           | Deploy to environments      |

---

## Troubleshooting Quick Fixes

### Runner Not Working

```bash
docker exec -it gitlab-runner gitlab-runner verify
docker restart gitlab-runner
```

### Clear Cache

```bash
# In GitLab UI
CI/CD → Pipelines → Clear runner caches
```

### View Runner Logs

```bash
docker logs -f gitlab-runner
```

### Test Docker Build Locally

```bash
# Build
docker build -t test-build .

# Verify
docker images | grep test-build
```

---

## Production Deployment Checklist

- [ ] Environment variables configured
- [ ] Database backup created
- [ ] Health checks passing
- [ ] Load balancer ready (if using)
- [ ] Monitoring alerts configured
- [ ] Rollback plan prepared
- [ ] Team notified

---

## Quick Reference URLs

- **Documentation**: [GITLAB_CICD_SETUP.md](./GITLAB_CICD_SETUP.md)
- **GitLab CI/CD Docs**: https://docs.gitlab.com/ee/ci/
- **GitLab Runner Docs**: https://docs.gitlab.com/runner/
- **Docker Hub**: https://hub.docker.com/

---

## Need Help?

1. Check [GITLAB_CICD_SETUP.md](./GITLAB_CICD_SETUP.md) for detailed instructions
2. View pipeline job logs in GitLab UI
3. Check runner status: `docker logs gitlab-runner`
4. Verify CI/CD variables are set correctly

---

**Pro Tips:**

- Use tags for production releases
- Always test in staging first
- Monitor pipeline execution times
- Keep runner updated
- Use protected variables for secrets
