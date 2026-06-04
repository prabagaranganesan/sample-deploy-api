# sample-deploy-api

Minimal Spring Boot API used to demo **two-phase ProxyHawk Guard onboarding**:

| Phase | What you have |
|-------|----------------|
| **Phase 1** (this repo) | Backend + CI with `test` → `deploy-staging` only |
| **Phase 2** | Add ProxyHawk Guard checkpoint after deploy |

## API

| Endpoint | Purpose |
|----------|---------|
| `GET /api/health` | Returns `gitSha` (deploy verify) |
| `GET /api/v1/profile` | Sample endpoint to Guard in Phase 2 |

## Phase 1 — Deploy pipeline (current)

```
push to main → test → deploy-staging (Render deploy hook + SHA verify)
```

### GitHub Actions config

Repo → **Settings → Secrets and variables → Actions**

**Variables**

| Name | Example |
|------|---------|
| `STAGING_DEPLOY_PLATFORM` | `render` |
| `STAGING_API_URL` | `https://sample-deploy-api.onrender.com` |

**Secrets**

| Name | Example |
|------|---------|
| `STAGING_DEPLOY_HOOK` | Render deploy hook URL |

### Render

1. Create web service from this repo (`render.yaml` included).
2. **Auto-Deploy → Off** (GitHub Actions triggers deploys).
3. Copy **Deploy Hook** → GitHub secret `STAGING_DEPLOY_HOOK`.

Verify Phase 1: push to `main` → Actions green through `deploy-staging`.

---

## Phase 2 — Add ProxyHawk Guard

**Public guide (any laptop):** https://proxyhawk.io/docs/guard-ci-existing-pipeline.html

Summary:

1. **Before bootstrap:** install ProxyHawk GitHub App + save deploy checkpoint in Mac app.
2. Run bootstrap (`guard-onboard-repo.sh` or Mac app **Add Guard to my repo**).
3. Merge PR → wire Guard job into **existing** `ci.yml`.
4. Add `PROXYHAWK_API_EMAIL`, `PROXYHAWK_API_PASSWORD` secrets.
5. Push to `main` → `test → deploy-staging → guard-checkpoint-staging`.

---

## Local dev

```bash
./gradlew bootRun
curl http://localhost:8080/api/health
curl http://localhost:8080/api/v1/profile
```
