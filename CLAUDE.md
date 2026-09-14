# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`danceden-bookings-svc` — the Booking service (Spring Boot 3 / Java 17) for a dance studio booking/enrollment platform. It's one of two services in the system; a separate FastAPI Payments service lives in a sibling repo and is not part of this codebase.

## Commands

Local Postgres (required before running the app or its tests against a real DB):
```
docker compose up -d      # starts Postgres on localhost:5432, db "bookingsdb", user/pass postgres/postgres
docker compose down       # stop it
```

Build / run:
```
mvn spring-boot:run        # run the app (port 8080)
mvn clean package          # build the jar
```

Tests:
```
mvn test                                   # run all tests
mvn test -Dtest=ClassName                  # run a single test class
mvn test -Dtest=ClassName#methodName       # run a single test method
```
`src/test` currently has no test classes yet.

## Architecture

- **Package-by-layer**, not package-by-feature: `controller/` for REST controllers, `model/` for JPA entities and enums. Follow this convention for new code (e.g. add a `repository/` or `service/` package at the top level, don't nest by feature) unless you're deliberately migrating the whole codebase to a different layout.
- **Schema is owned by Flyway, not Hibernate.** Migrations live in `src/main/resources/db/migration/`, named `V<n>__description.sql`, and are the single source of truth for the database schema — never hand-edit the schema or rely on Hibernate to create/alter tables. `spring.jpa.hibernate.ddl-auto=validate` in `application.properties` is intentional: Hibernate only verifies entity mappings match what Flyway already created, and fails startup on mismatch. Any schema change must go through a new numbered migration file, never an edit to an existing one that's already been applied.
- **Enum-typed entity fields use `@Enumerated(EnumType.STRING)`**, never `ORDINAL` — keep this consistent for new enum columns.
- Money fields are `BigDecimal` (with explicit `precision`/`scale`), never `float`/`double`.
