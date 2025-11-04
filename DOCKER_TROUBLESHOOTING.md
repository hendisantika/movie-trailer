# Docker Volume Error - Quick Fix Guide

## If You See This Error:

```
Error: error mounting "/path/to/postgres_data" to rootfs at "/var/lib/postgresql/data":
change mount propagation through procfd: no such file or directory: unknown
```

## Quick Fix (30 seconds)

### Step 1: Stop Everything

```bash
docker compose down -v
```

### Step 2: Verify Configuration

```bash
# Check that compose.yaml uses named volume (not bind mount)
grep -A2 "volumes:" compose.yaml
```

**Should show:**

```yaml
volumes:
  # Use named volume for cross-platform compatibility
  - postgres_data:/var/lib/postgresql/data
```

**Should NOT show:**

```yaml
volumes:
  - ./postgres_data:/var/lib/postgresql/data  # ❌ This causes the error
```

### Step 3: Start Fresh

```bash
docker compose up -d
```

### Step 4: Verify It Works

```bash
docker ps --filter "name=postgres-movie-trailers"
```

**Expected output:**

```
STATUS
Up X seconds (healthy)
```

## Why This Happens

**The error occurs when Docker tries to use a bind mount to a local directory that:**

1. Doesn't exist
2. Has permission issues
3. Is on a filesystem Docker can't access
4. Conflicts with Docker's overlay filesystem

**Solution:** Use Docker-managed **named volumes** instead of bind mounts.

## Configuration Comparison

### ❌ WRONG - Bind Mount (Causes Error)

```yaml
services:
  postgres:
    volumes:
      - ./postgres_data:/var/lib/postgresql/data  # Local directory
```

**Problems:**

- Requires local directory creation
- Permission issues on Linux/WSL
- Path resolution differences across OS
- May conflict with Docker overlay filesystem

### ✅ CORRECT - Named Volume (Always Works)

```yaml
services:
  postgres:
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Docker-managed

volumes:
  postgres_data:  # Declare the volume
```

**Benefits:**

- Works on all platforms
- No permission issues
- No local directory needed
- Docker handles everything

## Still Having Issues?

### Clear All Docker Cache

```bash
# Nuclear option - removes ALL containers, volumes, networks
docker compose down -v
docker system prune -a --volumes -f
docker compose up -d
```

### Check Docker is Running

```bash
docker --version
docker ps
```

### Check compose.yaml Syntax

```bash
docker compose config
```

### Enable Debug Mode

```bash
docker compose --verbose up -d
```

## Alternative: Use Bind Mount (Advanced)

If you **must** use a bind mount (for direct file access):

```bash
# Use the alternative configuration
docker compose -f compose.local.yaml up -d
```

**Before using, ensure:**

1. Directory exists: `mkdir -p postgres_data`
2. Proper permissions (Linux): `sudo chown -R 999:999 postgres_data`
3. Docker has file sharing enabled (Docker Desktop)

## Summary

| Action             | Command                                                                                                                  |
|--------------------|--------------------------------------------------------------------------------------------------------------------------|
| **Fix error**      | `docker compose down -v && docker compose up -d`                                                                         |
| **Check status**   | `docker ps \| grep postgres`                                                                                             |
| **View logs**      | `docker logs postgres-movie-trailers`                                                                                    |
| **Enter database** | `docker exec -it postgres-movie-trailers psql -U yu71 -d movie_trailers`                                                 |
| **Backup data**    | `docker run --rm -v movie-trailer_postgres_data:/data -v $(pwd):/backup alpine tar czf /backup/backup.tar.gz -C /data .` |
| **Restore data**   | `docker run --rm -v movie-trailer_postgres_data:/data -v $(pwd):/backup alpine tar xzf /backup/backup.tar.gz -C /data`   |

## Need More Help?

See the comprehensive guide: [DOCKER_VOLUME_GUIDE.md](./DOCKER_VOLUME_GUIDE.md)
