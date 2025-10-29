# Branch → Deployment Mapping

Quick reference for which branches trigger automatic deployments.

## 🎯 Auto-Deployment Branches

```
┌─────────────────────────────────────────────────────────────────┐
│                    AUTOMATIC DEPLOYMENTS                        │
└─────────────────────────────────────────────────────────────────┘

develop branch
    │
    ├─ Push detected
    │
    ├─ Build → Test → Package
    │
    └─ 🚀 AUTO-DEPLOY to staging
       └─ https://staging.movie-trailer.example.com


staging branch
    │
    ├─ Push detected
    │
    ├─ Build → Test → Package
    │
    └─ 🚀 AUTO-DEPLOY to staging
       └─ https://staging.movie-trailer.example.com


complete branch
    │
    ├─ Push detected
    │
    ├─ Build → Test → Package
    │
    └─ 🚀 AUTO-DEPLOY to complete environment
       └─ https://complete.movie-trailer.example.com


┌─────────────────────────────────────────────────────────────────┐
│                     MANUAL DEPLOYMENTS                          │
└─────────────────────────────────────────────────────────────────┘

main branch
    │
    ├─ Push detected
    │
    ├─ Build → Test → Package
    │
    └─ ⚠️  MANUAL TRIGGER REQUIRED
       └─ Click "Play" in GitLab UI
          └─ 🚀 Deploy to production
             └─ https://movie-trailer.example.com


tag (v*.*.*)
    │
    ├─ Tag created
    │
    ├─ Build → Test → Package
    │
    └─ ⚠️  MANUAL TRIGGER REQUIRED
       └─ Click "Play" in GitLab UI
          └─ 🚀 Deploy to production
             └─ https://movie-trailer.example.com
```

---

## 📊 Quick Reference Table

| Branch      | Auto Deploy   | Environment | URL                                | Notes                      |
|-------------|---------------|-------------|------------------------------------|----------------------------|
| `develop`   | ✅ Yes         | Staging     | staging.movie-trailer.example.com  | Main development branch    |
| `staging`   | ✅ Yes         | Staging     | staging.movie-trailer.example.com  | Alternative staging branch |
| `complete`  | ✅ Yes         | Complete    | complete.movie-trailer.example.com | Final testing before prod  |
| `main`      | ❌ No (Manual) | Production  | movie-trailer.example.com          | **Requires approval**      |
| `tags v*`   | ❌ No (Manual) | Production  | movie-trailer.example.com          | **Requires approval**      |
| `feature/*` | ❌ No          | -           | -                                  | No deployment              |
| `hotfix/*`  | ❌ No          | -           | -                                  | No deployment              |

---

## 🔄 Typical Workflow

```
┌─────────────┐
│   Feature   │
│   Branch    │
└──────┬──────┘
       │ Merge Request
       ↓
┌─────────────┐      ┌─────────────────┐
│   develop   │─────→│  🚀 Staging     │ (Automatic)
└──────┬──────┘      │  Environment    │
       │             └─────────────────┘
       │ Test in staging
       │
       │ Merge Request
       ↓
┌─────────────┐      ┌─────────────────┐
│  complete   │─────→│  🚀 Complete    │ (Automatic)
└──────┬──────┘      │  Environment    │
       │             └─────────────────┘
       │ Final testing
       │
       │ Merge Request
       ↓
┌─────────────┐      ┌─────────────────┐
│    main     │─────→│  ⚠️  Production  │ (Manual)
└─────────────┘      │  Click "Play"   │
                     └─────────────────┘
```

---

## 🎬 Usage Examples

### Deploy to Staging (develop)

```bash
git checkout develop
git pull origin develop
# Make changes
git add .
git commit -m "Add feature"
git push origin develop  # ⚡ AUTO-DEPLOYS!
```

### Deploy to Complete Environment

```bash
git checkout complete
git merge develop
git push origin complete  # ⚡ AUTO-DEPLOYS!
```

### Deploy to Production

```bash
git checkout main
git merge complete
git push origin main  # Builds but does NOT auto-deploy

# Then in GitLab UI:
# CI/CD → Pipelines → Click "Play" on deploy:production
```

---

## 🛡️ Safety Rules

1. **Production requires manual approval** - No accidents!
2. **Staging auto-deploys** - Fast feedback loop
3. **Complete environment** - Final testing ground
4. **Feature branches don't deploy** - Work safely

---

## 💡 Pro Tips

### Tip 1: Test Before Merging

```bash
# Create feature branch
git checkout -b feature/my-feature

# Work and test locally
# When ready, merge to develop
git checkout develop
git merge feature/my-feature
git push origin develop  # Auto-deploys to staging
```

### Tip 2: Use Complete for Final Testing

```bash
# Merge multiple features to complete
git checkout complete
git merge develop
git push origin complete  # Auto-deploys to complete env

# Test thoroughly
# If good, merge to main
```

### Tip 3: Emergency Hotfix

```bash
# Create hotfix from main
git checkout -b hotfix/urgent-fix main

# Fix the issue
git commit -m "Fix critical bug"

# Test in staging first
git checkout develop
git merge hotfix/urgent-fix
git push origin develop  # Auto-deploys to staging

# Test in staging
# If good, deploy to production
git checkout main
git merge hotfix/urgent-fix
git push origin main
# Manual deploy via GitLab UI
```

---

## 📋 Branch Protection Recommended

```yaml
Protected Branches (in GitLab):

main:
  - Allowed to merge: Maintainers
  - Allowed to push: No one
  - Require approval: Yes

develop:
  - Allowed to merge: Developers + Maintainers
  - Allowed to push: Developers + Maintainers

complete:
  - Allowed to merge: Developers + Maintainers
  - Allowed to push: Developers + Maintainers
```

---

**Legend:**

- ✅ = Automatic deployment
- ❌ = No automatic deployment
- ⚠️ = Manual trigger required
- 🚀 = Deployment happens
