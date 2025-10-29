# Movie Trailer CI/CD Configuration

## 📋 Overview

This project now has a complete GitLab CI/CD pipeline with **automatic deployment** capabilities. Push to specific
branches, and your code automatically deploys!

---

## 🚀 Quick Start

### Automatic Deployment Example

```bash
# Deploy to staging automatically
git checkout develop
git add .
git commit -m "Add new feature"
git push origin develop  # ⚡ Automatically deploys to staging!
```

### Manual Production Deployment

```bash
# Production requires approval (safety!)
git checkout main
git merge develop
git push origin main  # Builds but doesn't auto-deploy

# Go to GitLab UI: CI/CD → Pipelines
# Click "Play" on deploy:production job
```

---

## 🎯 Branch → Deployment Matrix

| Branch     | Deployment Type | Environment | Auto-Deploy |
|------------|-----------------|-------------|-------------|
| `develop`  | Automatic       | Staging     | ✅ Yes       |
| `staging`  | Automatic       | Staging     | ✅ Yes       |
| `complete` | Automatic       | Complete    | ✅ Yes       |
| `main`     | Manual          | Production  | ❌ No        |
| `v*` tags  | Manual          | Production  | ❌ No        |

---

## 📚 Documentation Files

| File                         | Purpose                                        |
|------------------------------|------------------------------------------------|
| **AUTO_DEPLOYMENT_GUIDE.md** | Complete guide to automatic deployments        |
| **BRANCH_DEPLOYMENT_MAP.md** | Quick reference for branch→environment mapping |
| **GITLAB_CICD_SETUP.md**     | Full CI/CD setup instructions                  |
| **CICD_QUICKSTART.md**       | 5-minute quick start guide                     |
| **CI_CD_SUMMARY.md**         | Pipeline overview and features                 |

---

## 🔄 Typical Workflow

```
1. Create feature
   ├─ feature/my-feature
   └─ Push (no deploy)

2. Merge to develop
   ├─ develop branch
   └─ 🚀 Auto-deploys to staging

3. Test in staging
   └─ https://staging.movie-trailer.example.com

4. Merge to complete
   ├─ complete branch  
   └─ 🚀 Auto-deploys to complete env

5. Final testing
   └─ https://complete.movie-trailer.example.com

6. Merge to main
   ├─ main branch
   └─ ⚠️ Manual deploy to production
```

---

## ⚙️ Pipeline Stages

```yaml
Stage 1: BUILD
  └─ Compile Java code with Maven

Stage 2: TEST
  ├─ Unit tests
  ├─ Integration tests (PostgreSQL)
  └─ Code quality checks

Stage 3: PACKAGE
  ├─ Create JAR file
  └─ Build Docker image (Jib)

Stage 4: DEPLOY
  ├─ Auto: develop → staging
  ├─ Auto: complete → complete
  └─ Manual: main → production
```

---

## 🛠️ Setup Requirements

### 1. GitLab Runner Installation

```bash
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest
```

### 2. Runner Registration

```bash
docker exec -it gitlab-runner gitlab-runner register
```

**Configuration:**

- URL: `https://gitlab.com/`
- Tags: `docker`
- Executor: `docker`
- Image: `maven:3.9-amazoncorretto-25`

### 3. CI/CD Variables

In GitLab: **Settings → CI/CD → Variables**

Required variables:

```
CI_REGISTRY_USER = your-username
CI_REGISTRY_PASSWORD = your-personal-access-token
SSH_PRIVATE_KEY = your-ssh-key
STAGING_SERVER = staging.yourdomain.com
STAGING_USER = deploy
PRODUCTION_SERVER = prod.yourdomain.com
PRODUCTION_USER = deploy
```

---

## 🔐 Security

- ✅ Production requires manual approval
- ✅ Protected CI/CD variables
- ✅ Non-root container user
- ✅ Health checks enabled
- ✅ Automatic environment cleanup

---

## 📊 Monitoring

### View Pipelines

```
GitLab → CI/CD → Pipelines
```

### View Environments

```
GitLab → Operations → Environments
```

### Check Deployment Status

```bash
# On server
docker-compose ps
docker-compose logs -f
```

---

## 🚨 Troubleshooting

### Pipeline Not Running

**Check:**

1. Runner status: `docker ps | grep gitlab-runner`
2. CI/CD variables configured
3. .gitlab-ci.yml syntax
4. Runner tags match job tags

### Deployment Failed

**Solutions:**

```bash
# View logs in GitLab UI
# CI/CD → Pipelines → Failed job → View logs

# Check server
ssh deploy@staging-server
cd /opt/movie-trailer
docker-compose logs
```

### Auto-Deploy Not Triggering

**Verify:**

1. Pushed to correct branch (develop/staging/complete)
2. Pipeline completed successfully
3. SSH access configured
4. Server variables set

---

## 💡 Best Practices

1. **Always test in staging first**
   ```bash
   git push origin develop  # Auto-deploys to staging
   # Test thoroughly
   # Then merge to main
   ```

2. **Use feature branches**
   ```bash
   git checkout -b feature/my-work
   # Work without triggering deployments
   ```

3. **Tag production releases**
   ```bash
   git tag -a v1.0.0 -m "Release 1.0.0"
   git push origin v1.0.0
   # Manual deploy via GitLab UI
   ```

4. **Monitor after deployment**
    - Check application health
    - Review logs for errors
    - Test critical features
    - Monitor for 15 minutes

---

## 🎓 Learn More

- [AUTO_DEPLOYMENT_GUIDE.md](./AUTO_DEPLOYMENT_GUIDE.md) - Detailed auto-deployment guide
- [BRANCH_DEPLOYMENT_MAP.md](./BRANCH_DEPLOYMENT_MAP.md) - Branch mapping reference
- [GITLAB_CICD_SETUP.md](./GITLAB_CICD_SETUP.md) - Complete setup guide
- [CICD_QUICKSTART.md](./CICD_QUICKSTART.md) - 5-minute quick start

---

## 🆘 Support

**Common Issues:**

- Deployment not triggering → Check branch name
- Pipeline failing → Check logs in GitLab
- Server errors → SSH to server and check logs

**Getting Help:**

1. Check documentation
2. Review pipeline logs
3. Verify CI/CD variables
4. Contact DevOps team

---

## 📝 Change Log

### v1.0.0 (2025-10-29)

- ✅ Initial CI/CD setup
- ✅ Automatic deployment for develop, staging, complete branches
- ✅ Manual deployment for production
- ✅ Docker containerization with Jib
- ✅ Comprehensive documentation

---

**🎬 Your Movie Trailer application is production-ready with CI/CD! 🚀**

Push to `develop` or `complete` branch to see automatic deployment in action!
