# Docker Volume Configuration Guide

## Problem: Volume Mount Errors on Some Clients

### Error Message

```
Error response from daemon: failed to create task for container: failed to create shim task:
OCI runtime create failed: runc create failed: unable to start container process:
error during container init: error mounting "/path/to/postgres_data" to rootfs at
"/var/lib/postgresql/data": change mount propagation through procfd:
open o_path procfd: open /var/lib/docker/overlay2/.../merged/var/lib/postgresql/data:
no such file or directory: unknown
```

### Root Cause

This error occurs when using **bind mounts** (`./postgres_data`) due to:

- Local directory doesn't exist before container starts
- Permission issues on Linux/WSL systems
- Docker overlay filesystem conflicts
- Path resolution differences across OS (Windows/Mac/Linux)

## Solution: Use Named Volumes (Default)

### ✅ Recommended: `compose.yaml` (Named Volume)

The default `compose.yaml` now uses a **named volume** which works across all platforms:

```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
```

**Advantages:**

- ✅ Works on all OS (Windows, Mac, Linux)
- ✅ No permission issues
- ✅ Docker manages volume lifecycle
- ✅ Better performance on Docker Desktop
- ✅ No local directory needed

**Usage:**

```bash
docker compose up -d
```

**Volume Management:**

```bash
# List volumes
docker volume ls | grep postgres_data

# Inspect volume
docker volume inspect movie-trailer_postgres_data

# Backup volume
docker run --rm -v movie-trailer_postgres_data:/data -v $(pwd):/backup \
  alpine tar czf /backup/postgres_backup.tar.gz -C /data .

# Restore volume
docker run --rm -v movie-trailer_postgres_data:/data -v $(pwd):/backup \
  alpine tar xzf /backup/postgres_backup.tar.gz -C /data
```

## Alternative: Bind Mount (Optional)

### 📁 For Local Development: `compose.local.yaml` (Bind Mount)

If you need direct access to database files, use the alternative configuration:

```yaml
volumes:
  - ./postgres_data:/var/lib/postgresql/data
```

**Advantages:**

- ✅ Easy access to database files
- ✅ Simple file-based backups
- ✅ Can inspect data directly

**Disadvantages:**

- ❌ May fail on some systems
- ❌ Permission issues on Linux
- ❌ Requires directory creation

**Before using:**

```bash
# Create directory first
mkdir -p postgres_data

# Ensure proper permissions (Linux/WSL only)
chmod 777 postgres_data
```

**Usage:**

```bash
docker compose -f compose.local.yaml up -d
```

## Troubleshooting

### Issue 1: Permission Denied (Linux/WSL)

**Error:**

```
ERROR: mkdir /var/lib/postgresql/data: permission denied
```

**Solution:**

```bash
# Option 1: Create directory with proper ownership
mkdir -p postgres_data
sudo chown -R 999:999 postgres_data

# Option 2: Use named volume (recommended)
docker compose up -d  # Uses default compose.yaml
```

### Issue 2: Volume Already Exists

**Error:**

```
ERROR: Volume movie-trailer_postgres_data already exists
```

**Solution:**

```bash
# Remove old volume
docker compose down -v

# Start fresh
docker compose up -d
```

### Issue 3: Data Persistence

**To keep data between restarts:**

```bash
# Stop without removing volumes
docker compose stop

# Start again (data persists)
docker compose start
```

**To remove all data:**

```bash
# Stop and remove volumes
docker compose down -v
```

## Which Configuration Should I Use?

### Use `compose.yaml` (Named Volume) if:

- ✅ You want maximum compatibility
- ✅ You're deploying to production
- ✅ You're using Docker Desktop (Mac/Windows)
- ✅ You don't need direct file access
- ✅ You're having mount errors

### Use `compose.local.yaml` (Bind Mount) if:

- ✅ You need to inspect database files
- ✅ You want simple file-based backups
- ✅ You're on Linux with proper permissions
- ✅ You're comfortable troubleshooting mount issues

## Best Practices

### Development

```bash
# Use named volume for stability
docker compose up -d
```

### Production

```bash
# Use named volume + backup strategy
docker compose up -d

# Schedule regular backups
docker run --rm -v movie-trailer_postgres_data:/data -v /backups:/backup \
  alpine tar czf /backup/postgres-$(date +%Y%m%d).tar.gz -C /data .
```

### CI/CD

```yaml
# GitHub Actions example
- name: Start Database
  run: docker compose up -d

- name: Wait for healthy
  run: |
    timeout 30 bash -c 'until docker exec postgres-movie-trailers pg_isready -U yu71; do sleep 1; done'
```

## Summary

| Feature             | Named Volume       | Bind Mount       |
|---------------------|--------------------|------------------|
| **Cross-platform**  | ✅ Yes              | ❌ No             |
| **No setup needed** | ✅ Yes              | ❌ No             |
| **Performance**     | ✅ Fast             | ⚠️ Varies        |
| **Direct access**   | ❌ No               | ✅ Yes            |
| **Permissions**     | ✅ Easy             | ❌ Complex        |
| **Backups**         | ⚠️ Docker commands | ✅ File copy      |
| **Recommended**     | ✅ Default          | ⚠️ Special cases |

**Default choice:** Use `compose.yaml` with named volumes for best results!
