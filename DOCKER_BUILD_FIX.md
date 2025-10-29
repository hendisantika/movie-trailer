# Docker Build Error Fix

## 🐛 Error: "docker: command not found"

### Problem

When using Maven image with Docker-in-Docker, the error occurs because:

- Maven image doesn't include Docker CLI
- Need either Docker daemon OR use Jib (which doesn't need Docker)

### ✅ Solution Applied

I've fixed the `.gitlab-ci.yml` to use **Jib exclusively** for the main Docker build job. Jib doesn't require Docker
daemon, making it faster and more reliable in CI/CD.

---

## What Changed

### Before (Broken)

```yaml
package:docker:
  image: maven:3.9-amazoncorretto-25
  services:
    - docker:24-dind  # Trying to use Docker
  before_script:
    - docker login ...  # ❌ docker command not found!
```

### After (Fixed)

```yaml
package:docker:
  image: maven:3.9-amazoncorretto-25
  script:
    - mvn compile jib:build ...  # ✅ No Docker needed!
```

---

## Why This Works

**Jib advantages:**

- No Docker daemon required
- No Dockerfile needed
- Faster builds (better caching)
- More reliable in CI/CD
- Directly pushes to registry

**Jib builds containerized Java apps without Docker!**

---

## Alternative: If You Need Docker Commands

If you specifically need Docker CLI (for the manual build job), use this approach:

```yaml
package:docker-manual:
  image: docker:24  # Use Docker image instead of Maven
  services:
    - docker:24-dind
  before_script:
    # Install Maven in Docker image
    - apk add --no-cache maven openjdk21
  script:
    - mvn clean package -DskipTests
    - docker build -t $IMAGE_TAG .
    - docker push $IMAGE_TAG
```

---

## Verification

After the fix, your pipeline should:

1. ✅ Build successfully with Jib
2. ✅ Push image to GitLab Container Registry
3. ✅ No "docker: command not found" errors

---

## Testing

Push to your repository and watch the pipeline:

```bash
git add .gitlab-ci.yml
git commit -m "Fix Docker build with Jib"
git push origin develop
```

Watch in GitLab:

```
CI/CD → Pipelines → package:docker job
```

Should see:

```
Building Docker image with Jib (no Docker daemon required)...
[INFO] Built and pushed image as ...
```

---

## Benefits of This Approach

1. **Faster** - Jib is optimized for Java apps
2. **More Reliable** - No Docker-in-Docker complexity
3. **Better Caching** - Layer-based caching
4. **No Dockerfile Required** - Jib configures everything
5. **Works Everywhere** - No special runner requirements

---

**Status:** ✅ Fixed - Pipeline should now work!
