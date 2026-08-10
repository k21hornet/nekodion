# Nekodion

A personal finance management app (family budget tracker).

## Repository Structure

```
nekodion
├─ fluffy    # Database (Flyway migrations)
├─ pounce    # Docker Compose environments (local, prod)
├─ prowl     # Backend (Java / Spring Boot)
├─ scratch   # Documentation (design, requirements, coding conventions)
└─ whisker   # Frontend (Next.js)
```

## Backend (`prowl/`)

**Stack:** Java 25, Spring Boot 4.0.5, Spring Data JPA, Lombok, Gradle multi-project

**Modules:**

| Module     | Responsibility                                       |
| ---------- | ---------------------------------------------------- |
| `api`      | REST controllers, use cases, request/response models |
| `batch`    | Scheduled batch jobs                                 |
| `core`     | Entities, repositories, core services                |
| `external` | Email parsing, CSV parsing, web scraping             |
| `ingest`   | Inbound webhook for card statement emails (Cloudflare Email Worker → `/api/email/*`) |
| `support`  | Shared utilities                                     |

**Module dependency direction:** `api` / `batch` → `core` / `external` → `support`. Never create reverse dependencies.

**Running the API (IntelliJ):**

- Task: `api:bootRun`, Gradle project: `prowl`
- Env vars: `DB_NAME=nekodion;DB_USERNAME=;DB_PASSWORD=;ISSUER=;AUDIENCE=`

**Running batch jobs:**

- Task: `batch:bootRun --args='--batch.execute=<name>'`, Gradle project: `prowl`
- Env vars: `DB_NAME=nekodion;DB_USERNAME=;DB_PASSWORD=`

**Running the ingest webhook (IntelliJ):**

- Task: `ingest:bootRun`, Gradle project: `prowl`
- Env vars: `DB_NAME=nekodion;DB_USERNAME=;DB_PASSWORD=;EMAIL_INBOUND_SECRET=`
- Requests must include the `X-Ingest-Secret` header matching `EMAIL_INBOUND_SECRET`

**Batch jobs:**

| Batch        | Argument   |
| ------------ | ---------- |
| Hello World  | HelloWorld |
| Gmail Import | gmail      |

## Frontend (`whisker/`)

**Stack:** Next.js 16 (App Router), React 19, TypeScript, Tailwind CSS v4, shadcn/ui, Auth0, pnpm

**Key directories:**

- `src/app/(authorized)/` — authenticated pages
- `src/app/(unauthorized)/` — public pages
- `src/features/<name>/` — feature modules (`api.ts`, `actions.ts`, `types.ts`, `components/`)
- `src/components/ui/` — shadcn/ui components (auto-generated, do not edit manually)
- `src/util/fetcher/` — HTTP client wrapper (use this instead of raw `fetch`)

**Running the frontend:**

```bash
cd whisker
pnpm dev
```

## Database (`fluffy/`)

- Managed by Flyway, run via Docker
- Migration files: `fluffy/src/main/resources/db/migration/`
- Naming: `V<major>_<minor>__<description>.sql`

## Local Environment (`pounce/`)

- `pounce/local/` — local Docker Compose (MySQL + Flyway)
- `pounce/prod/` — production Docker Compose

## Key Conventions

- **Cross-module dependencies:** core modules must not reference each other. Use IDs only across module boundaries.
- **User authentication:** Use `@CurrentUser UserDto currentUser` in controllers. Never access `SecurityContextHolder` directly.
- **API calls (frontend):** Always use `fetcher` from `@/util/fetcher`. Never use raw `fetch`.
- **Server Actions:** Must have `"use server"` at the top. Return typed state objects with `errors` and optional `success`.
- **`components/ui/`:** shadcn/ui only — do not hand-edit files in this directory.

## Documentation

Full coding conventions are in `scratch/コーディング規約/`:

- [バックエンド.md](scratch/コーディング規約/バックエンド.md)
- [フロントエンド.md](scratch/コーディング規約/フロントエンド.md)

Design docs are in `scratch/設計/`.
