# GitLab Runner Troubleshooting Guide

## 🚨 Error: "No active runners online"

This error means GitLab cannot find a runner with the required tag (`docker`) to execute your pipeline.

---

## 🔍 Diagnosis Steps

### Step 1: Check Runner Status in GitLab

**In GitLab UI:**

```
Your Project → Settings → CI/CD → Runners → Expand
```

**Look for:**

- ✅ Green dot = Runner is active
- ⚪ Gray dot = Runner is offline
- 🏷️ Tags = Must include "docker"

---

## ✅ Solutions

### Solution 1: Install GitLab Runner (If Not Installed)

#### Option A: Docker Installation (Recommended)

```bash
# 1. Pull and run GitLab Runner
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest

# 2. Verify it's running
docker ps | grep gitlab-runner
```

#### Option B: Native Installation (Linux)

```bash
# Ubuntu/Debian
curl -L "https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh" | sudo bash
sudo apt-get install gitlab-runner

# Start the runner
sudo gitlab-runner start
```

#### Option C: macOS Installation

```bash
# Download
sudo curl --output /usr/local/bin/gitlab-runner \
  "https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-darwin-amd64"

# Make executable
sudo chmod +x /usr/local/bin/gitlab-runner

# Install and start
cd ~
gitlab-runner install
gitlab-runner start
```

---

### Solution 2: Register the Runner

After installation, you MUST register it:

#### Get Registration Token

**In GitLab:**

```
Project → Settings → CI/CD → Runners → Expand
```

Copy the **registration token** (looks like: `GR1348941...`)

#### Register Runner

```bash
# If using Docker runner:
docker exec -it gitlab-runner gitlab-runner register

# If native installation:
sudo gitlab-runner register
```

#### Interactive Prompts:

```
Enter GitLab instance URL:
  https://gitlab.com/

Enter registration token:
  [Paste your token from GitLab]

Enter a description:
  movie-trailer-runner

Enter tags (comma-separated):
  docker                    ⚠️ IMPORTANT: Must include "docker"!

Enter executor:
  docker

Enter default Docker image:
  maven:3.9-amazoncorretto-25
```

**CRITICAL:** Make sure you enter `docker` as a tag!

---

### Solution 3: Verify Runner Configuration

#### Check Runner Status

```bash
# Docker runner
docker exec -it gitlab-runner gitlab-runner verify

# Native runner
sudo gitlab-runner verify
```

**Expected output:**

```
Verifying runner... is alive                        runner=xxx
```

#### Check Runner Configuration

```bash
# Docker runner
docker exec -it gitlab-runner cat /etc/gitlab-runner/config.toml

# Native runner
sudo cat /etc/gitlab-runner/config.toml
```

**Look for:**

```toml
[[runners]]
name = "movie-trailer-runner"
url = "https://gitlab.com/"
token = "YOUR_TOKEN"
executor = "docker"
[runners.docker]
image = "maven:3.9-amazoncorretto-25"
tags = ["docker"]  # ⚠️ MUST have "docker" tag!
```

---

### Solution 4: Use GitLab.com Shared Runners

If you don't want to set up your own runner, use GitLab's shared runners:

#### Enable Shared Runners

**In GitLab:**

```
Project → Settings → CI/CD → Runners → Expand
→ Enable "Shared runners"
```

**Note:** Free tier has 400 CI/CD minutes per month.

---

### Solution 5: Fix Offline Runner

If runner shows as offline:

#### Restart Runner

```bash
# Docker runner
docker restart gitlab-runner

# Native runner
sudo gitlab-runner restart
```

#### Check Runner Logs

```bash
# Docker runner
docker logs -f gitlab-runner

# Native runner
sudo gitlab-runner --debug run
```

---

## 🛠️ Complete Setup Example

Here's a full working example:

```bash
# 1. Install Docker runner
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest

# 2. Get registration token from GitLab UI
# Settings → CI/CD → Runners

# 3. Register runner (interactive)
docker exec -it gitlab-runner gitlab-runner register

# Enter:
# URL: https://gitlab.com/
# Token: [your token]
# Description: movie-trailer-runner
# Tags: docker
# Executor: docker
# Image: maven:3.9-amazoncorretto-25

# 4. Verify
docker exec -it gitlab-runner gitlab-runner verify

# 5. Check status
docker ps | grep gitlab-runner

# 6. Go to GitLab and trigger pipeline
```

---

## 🔍 Verification Checklist

After setup, verify:

- [ ] Runner appears in GitLab UI (Settings → CI/CD → Runners)
- [ ] Runner shows green dot (active)
- [ ] Runner has "docker" tag
- [ ] Runner is assigned to your project
- [ ] Docker executor is configured
- [ ] Runner logs show no errors

---

## 🚀 Quick Fix for Testing

Want to test immediately? Use GitLab's shared runners:

```yaml
# In .gitlab-ci.yml, jobs will use:
tags:
  - docker  # Will match shared runners with "docker" tag
```

**Enable in GitLab:**

```
Settings → CI/CD → Runners → Enable shared runners
```

Then re-run your pipeline!

---

## 💡 Alternative: Use GitLab.com Shared Runners

If setting up your own runner is too complex:

### Step 1: Enable Shared Runners

```
Project → Settings → CI/CD → Runners
→ Toggle "Enable shared runners for this project"
```

### Step 2: Modify Tags (if needed)

If shared runners don't have "docker" tag, update your jobs:

```yaml
# Change from:
tags:
  - docker

# To:
tags:
  - saas-linux-small-amd64  # GitLab.com shared runner tag
```

---

## 🐛 Common Issues

### Issue 1: Runner is registered but not showing

**Solution:**

```bash
# Restart runner
docker restart gitlab-runner

# Force update
docker exec -it gitlab-runner gitlab-runner verify --delete
docker exec -it gitlab-runner gitlab-runner register
```

### Issue 2: "docker: command not found" in runner

**Solution:**
Runner needs access to Docker daemon:

```bash
docker run -d --name gitlab-runner --restart always \
  -v /var/run/docker.sock:/var/run/docker.sock \  # ⚠️ This is required!
  -v gitlab-runner-config:/etc/gitlab-runner \
  gitlab/gitlab-runner:latest
```

### Issue 3: Runner starts then stops

**Solution:**
Check logs:

```bash
docker logs gitlab-runner

# Common causes:
# - Wrong token
# - Network issues
# - Docker daemon not accessible
```

### Issue 4: "This job is stuck" even with active runner

**Solutions:**

1. Check tags match exactly
2. Restart runner: `docker restart gitlab-runner`
3. Re-register runner
4. Check runner has correct permissions

---

## 📚 Additional Resources

- [GitLab Runner Docs](https://docs.gitlab.com/runner/)
- [Runner Registration](https://docs.gitlab.com/runner/register/)
- [Docker Executor](https://docs.gitlab.com/runner/executors/docker.html)

---

## 🆘 Still Not Working?

### Debug Mode

```bash
# Run runner in debug mode
docker exec -it gitlab-runner gitlab-runner --debug run
```

### Check Configuration

```bash
# View full config
docker exec -it gitlab-runner cat /etc/gitlab-runner/config.toml

# Expected structure:
concurrent = 1
check_interval = 0

[[runners]]
  name = "movie-trailer-runner"
  url = "https://gitlab.com/"
  token = "RUNNER_TOKEN"
  executor = "docker"
  [runners.docker]
    tls_verify = false
    image = "maven:3.9-amazoncorretto-25"
    privileged = false
    disable_entrypoint_overwrite = false
    oom_kill_disable = false
    disable_cache = false
    volumes = ["/cache"]
    shm_size = 0
```

---

## ✅ Success Indicators

You know it's working when:

1. ✅ Runner shows in GitLab with green dot
2. ✅ Pipeline jobs start executing
3. ✅ No "stuck" message
4. ✅ Build logs appear

---

**Last Updated:** 2025-10-29
**Version:** 1.0.0
