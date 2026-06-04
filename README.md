# sample-deploy-api

Minimal Spring Boot API for **two-phase ProxyHawk Guard onboarding**.

| Phase | Pipeline |
|-------|----------|
| **Phase 1** | `test → deploy-staging` |
| **Phase 2** | `test → deploy-staging → guard-checkpoint-staging` |

## API

| Endpoint | Purpose |
|----------|---------|
| `GET /api/health` | Returns `gitSha` (deploy verify) |
| `GET /api/v1/profile` | Sample endpoint to Guard in Phase 2 |

---

## Phase 1 — Deploy (done)

```
push main → test → deploy-staging
```

GitHub → **Settings → Secrets and variables → Actions**

| Type | Name | Example |
|------|------|---------|
| **Variable** | `STAGING_DEPLOY_PLATFORM` | `render` |
| **Variable** | `STAGING_API_URL` | `https://sample-deploy-api.onrender.com` |
| **Secret** | `STAGING_DEPLOY_HOOK` | Render deploy hook |

**Note:** `STAGING_API_URL` must be a **Variable**, not a Secret.

```bash
curl -sS https://sample-deploy-api.onrender.com/api/health | python3 -m json.tool
curl -sS https://sample-deploy-api.onrender.com/api/v1/profile | python3 -m json.tool
```

---

## Phase 2 — Add ProxyHawk Guard

**Full guide:** https://proxyhawk.io/docs/guard-ci-existing-pipeline.html  
**CI install:** https://proxyhawk.io/guard-ci/

### 1–3. Mac app prerequisites (before CI changes)

1. **Guard** tab → record `GET /api/v1/profile`
2. **Break alerts** → **Set up** → **Session routing** → Environment label = **`staging`** → **Save**  
   (empty = `dev` → mapping fails)
3. **Deploy checkpoint (CI)** → `prabagaranganesan` / `sample-deploy-api` → confirm Environment **`staging`** → **Save deploy checkpoint**

### 4. Install Guard CI files (any laptop)

One command installs **workflows + guard-runner**:

```bash
curl -fsSL https://proxyhawk.io/guard-ci/install.sh | bash
git add .github/workflows/ tools/guard-runner/
git commit -m "Add ProxyHawk Guard CI" && git push
```

### 5. Wire your pipeline workflow

Job and file names are flexible — `needs:` must match your deploy job id.

```yaml
permissions:
  id-token: write
  contents: read
  pull-requests: write

jobs:
  # … existing test, deploy-staging jobs …

  guard-staging:
    needs: deploy-staging
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    uses: ./.github/workflows/guard-post-backend-deploy.yml
    with:
      environment: staging
      commit_sha: ${{ github.sha }}
    secrets: inherit
```

### 6. Secrets

Add: `PROXYHAWK_API_EMAIL`, `PROXYHAWK_API_PASSWORD`

### 7. Verify

Push to `main` — all three jobs green.

---

## Local dev

```bash
./gradlew bootRun
curl http://localhost:8080/api/health
curl http://localhost:8080/api/v1/profile
```
