# Automatic Deployment Guide

## Overview

The Movie Trailer application now supports **automatic CI/CD deployments** when pushing to specific branches. This guide
explains how automatic deployments work and how to use them.

---

## 🚀 Automatic Deployment Branches

### Branch Deployment Matrix

| Branch      | Deployment | Trigger        | Environment | URL                                        |
|-------------|------------|----------------|-------------|--------------------------------------------|
| `develop`   | ✅ **Auto** | Push           | Staging     | https://staging.movie-trailer.example.com  |
| `staging`   | ✅ **Auto** | Push           | Staging     | https://staging.movie-trailer.example.com  |
| `complete`  | ✅ **Auto** | Push           | Complete    | https://complete.movie-trailer.example.com |
| `main`      | ⚠️ Manual  | Manual trigger | Production  | https://movie-trailer.example.com          |
| `tags (v*)` | ⚠️ Manual  | Manual trigger | Production  | https://movie-trailer.example.com          |

---

## ✅ Automatic Deployments

### 1. Deploy to Staging (develop branch)

**When:** Automatically triggers on every push to `develop` branch
**Where:** Staging environment

```bash
# Switch to develop branch
git checkout develop

# Make changes
git add .
git commit -m "Add new feature"

# Push - THIS WILL AUTO-DEPLOY! ⚡
git push origin develop
```

**Pipeline Flow:**

```
Push to develop
  ↓
Build → Test → Package → 🚀 Auto-Deploy to Staging
```

**What Happens:**

1. Code compiles
2. Tests run (unit + integration)
3. Docker image builds
4. **Automatically deploys to staging server**
5. Old containers replaced
6. Health checks verified

### 2. Deploy to Staging (staging branch)

**When:** Automatically triggers on every push to `staging` branch
**Where:** Staging environment

```bash
# Create or switch to staging branch
git checkout -b staging

# Make changes
git add .
git commit -m "Testing deployment"

# Push - THIS WILL AUTO-DEPLOY! ⚡
git push origin staging
```

### 3. Deploy to Complete Environment

**When:** Automatically triggers on every push to `complete` branch
**Where:** Complete environment (separate from staging)

```bash
# Switch to complete branch
git checkout -b complete

# Make changes
git add .
git commit -m "Complete implementation"

# Push - THIS WILL AUTO-DEPLOY! ⚡
git push origin complete
```

**Special Features:**

- Dedicated environment URL: `https://complete.movie-trailer.example.com`
- Auto-stops after 7 days of inactivity
- Can be manually stopped via GitLab UI

---

## ⚠️ Manual Deployments

### Production Deployment (main branch)

**When:** Manual trigger required (safety measure)
**Where:** Production environment

```bash
# Merge to main
git checkout main
git merge develop

# Push (builds but does NOT auto-deploy)
git push origin main

# Go to GitLab UI:
# CI/CD → Pipelines → Latest Pipeline
# Click "Play" button on "deploy:production" job
```

**Why Manual?**

- Production requires human verification
- Prevents accidental deployments
- Allows time for final checks
- Requires explicit approval

### Tagged Releases

```bash
# Create a release tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# In GitLab UI:
# Click "Play" on deploy:production job
```

---

## 📊 Deployment Workflow Examples

### Example 1: Feature Development

```bash
# 1. Create feature branch
git checkout -b feature/user-profile

# 2. Develop and test locally
# ... make changes ...

# 3. Push to GitLab (no auto-deploy)
git push origin feature/user-profile

# 4. Create Merge Request to develop
# ... review and approve ...

# 5. Merge to develop - AUTO-DEPLOYS to staging! ⚡
# Pipeline runs automatically

# 6. Test in staging environment
# Visit: https://staging.movie-trailer.example.com

# 7. If good, merge to main
git checkout main
git merge develop
git push origin main

# 8. Manually deploy to production
# GitLab UI → Click "Play" on deploy:production
```

### Example 2: Hotfix

```bash
# 1. Create hotfix branch from main
git checkout main
git checkout -b hotfix/critical-bug

# 2. Fix the bug
git add .
git commit -m "Fix critical security issue"

# 3. Test locally first!

# 4. Merge to develop for testing
git checkout develop
git merge hotfix/critical-bug
git push origin develop
# ⚡ Auto-deploys to staging

# 5. Test in staging

# 6. If good, merge to main
git checkout main
git merge hotfix/critical-bug
git push origin main

# 7. Manually deploy to production
# GitLab UI → Click "Play" on deploy:production
```

### Example 3: Using Complete Branch

```bash
# 1. When feature is complete and tested
git checkout complete
git merge feature/my-feature

# 2. Push - AUTO-DEPLOYS! ⚡
git push origin complete

# 3. Complete environment available at:
# https://complete.movie-trailer.example.com

# 4. Final testing before production

# 5. Merge to main when ready
git checkout main
git merge complete
git push origin main

# 6. Manual production deployment
```

---

## 🔍 Monitoring Deployments

### Check Pipeline Status

**GitLab UI:**

```
Project → CI/CD → Pipelines
```

**Command Line:**

```bash
# View recent pipelines
git log --oneline -10

# Check GitLab pipeline status
# Visit: https://gitlab.com/yourusername/movie-trailer/-/pipelines
```

### View Deployment Logs

**In GitLab:**

1. Go to **CI/CD → Pipelines**
2. Click on the pipeline
3. Click on `deploy:staging:auto` or `deploy:complete:auto`
4. View logs

### Environment Status

**GitLab UI:**

```
Operations → Environments
```

Shows:

- Active environments
- Last deployment time
- Deployment status
- Quick links to environments

---

## 🛑 Stopping Auto-Deployments

### Temporary Disable

**Option 1: Don't push to auto-deploy branches**

```bash
# Work on feature branches instead
git checkout -b feature/my-work
git push origin feature/my-work
# No auto-deploy triggered
```

**Option 2: Use manual staging**

```bash
# Push to main (no auto-deploy)
git push origin main

# Manually trigger staging if needed
# GitLab UI → deploy:staging:manual → Click "Play"
```

### Stop Complete Environment

```bash
# In GitLab UI:
# Operations → Environments → complete
# Click "Stop" button

# Or use the stop job:
# CI/CD → Pipelines → stop:complete → Click "Play"
```

---

## ⚙️ Configuration

### CI/CD Variables Required

For automatic deployments to work, these variables must be set:

**In GitLab:** `Settings → CI/CD → Variables`

```bash
# SSH Access
SSH_PRIVATE_KEY = <your-ssh-private-key>
STAGING_SERVER = staging.yourdomain.com
STAGING_USER = deploy

# For production (manual)
PRODUCTION_SERVER = prod.yourdomain.com
PRODUCTION_USER = deploy

# Docker Registry
CI_REGISTRY_USER = your-username
CI_REGISTRY_PASSWORD = your-token
```

### Server Requirements

**Staging Server:**

- Docker & Docker Compose installed
- SSH access configured
- Deployment directory: `/opt/movie-trailer`
- Ports 8080, 5432 accessible

**Production Server:**

- Same as staging
- Additional security hardening
- Backup system in place

---

## 🔒 Security Considerations

### Auto-Deployment Safety

✅ **Safe:**

- `develop` → staging (isolated environment)
- `staging` → staging (isolated environment)
- `complete` → complete (temporary environment)

⚠️ **Protected:**

- `main` → production (manual approval required)
- `tags` → production (manual approval required)

### Branch Protection

**Recommended GitLab Settings:**

```
Settings → Repository → Protected Branches

main:
  - Allowed to merge: Maintainers only
  - Allowed to push: No one
  - Require approval: Yes

develop:
  - Allowed to merge: Developers + Maintainers
  - Allowed to push: Developers + Maintainers
```

---

## 🚨 Troubleshooting

### Deployment Not Triggered

**Problem:** Pushed to develop but no deployment

**Check:**

1. Pipeline status in GitLab
2. Runner is active: `docker ps | grep gitlab-runner`
3. CI/CD variables are set
4. No syntax errors in .gitlab-ci.yml

### Deployment Failed

**Problem:** Deployment job failed

**Solutions:**

```bash
# Check job logs in GitLab

# Verify SSH access
ssh deploy@staging-server

# Check server
ssh deploy@staging-server
cd /opt/movie-trailer
docker-compose ps
docker-compose logs
```

### Wrong Environment Deployed

**Problem:** Code deployed to wrong environment

**Solution:**

```bash
# Verify branch
git branch

# Check remote
git remote -v

# Re-deploy correct version
git checkout correct-branch
git push origin correct-branch
```

---

## 📋 Deployment Checklist

### Before Pushing to Auto-Deploy Branch

- [ ] Code reviewed and approved
- [ ] Tests passing locally
- [ ] No sensitive data in commits
- [ ] Database migrations prepared (if any)
- [ ] Breaking changes documented
- [ ] Team notified

### After Auto-Deployment

- [ ] Check pipeline status (all green)
- [ ] Verify application health
- [ ] Test critical features
- [ ] Check logs for errors
- [ ] Monitor for 15 minutes
- [ ] Update team

---

## 💡 Best Practices

### 1. Feature Development

```bash
feature/* → develop (MR) → [AUTO-DEPLOY] staging → main (MR) → [MANUAL] production
```

### 2. Hotfixes

```bash
hotfix/* → develop → [AUTO-DEPLOY] staging → test → main → [MANUAL] production
```

### 3. Releases

```bash
release/* → develop → [AUTO-DEPLOY] staging → test → main + tag → [MANUAL] production
```

### 4. Testing Complete Features

```bash
develop → complete → [AUTO-DEPLOY] complete env → test → main → [MANUAL] production
```

---

## 🔄 Rollback Procedures

### Staging Rollback

**Automatic:** Next push overwrites

**Manual:**

```bash
# Revert commit
git revert <commit-hash>
git push origin develop
# Auto-deploys reverted version
```

### Production Rollback

```bash
# In GitLab UI:
# CI/CD → Pipelines
# Find "rollback:production" job
# Click "Play"

# Or revert and deploy
git revert <commit-hash>
git push origin main
# Manual deploy through GitLab UI
```

---

## 📊 Pipeline Summary

```yaml
Branch: develop
  ├── build ✓
  ├── test ✓
  ├── package ✓
  └── deploy:staging:auto ⚡ AUTOMATIC

Branch: complete
  ├── build ✓
  ├── test ✓
  ├── package ✓
  └── deploy:complete:auto ⚡ AUTOMATIC

Branch: main
  ├── build ✓
  ├── test ✓
  ├── package ✓
  └── deploy:production ⚠️  MANUAL (Click "Play")
```

---

## 📚 Related Documentation

- [GitLab CI/CD Setup Guide](./GITLAB_CICD_SETUP.md)
- [Quick Start Guide](./CICD_QUICKSTART.md)
- [CI/CD Summary](./CI_CD_SUMMARY.md)

---

## 🆘 Support

### Common Questions

**Q: How do I disable auto-deployment temporarily?**
A: Don't push to `develop`, `staging`, or `complete` branches. Use feature branches instead.

**Q: Can I auto-deploy to production?**
A: No, production always requires manual approval for safety.

**Q: What if staging deployment fails?**
A: Check logs, fix issues, push again. Staging won't affect production.

**Q: How do I test without deploying?**
A: Push to feature branches or use main branch (won't auto-deploy).

---

**Last Updated:** 2025-10-29
**Version:** 1.0.0
**Pipeline:** Auto-deployment enabled ✅
