# HomeAway

HomeAway helps students find useful places around campus: somewhere to stay,
grab a meal, or spend time with the local community.

This repository contains two layers:

- The original Java command-line coursework app and its custom data structures.
- `mvp/`, a separate web MVP with a Spring Boot API and a React + TypeScript UI.

The web MVP is deliberately isolated from the coursework source, so its web
framework and database do not change the ADT implementation.

## Run the web MVP

Requirements: Java 21 or newer and Node.js 20 or newer.

In one terminal, start the API:

```sh
cd mvp/api
./mvnw spring-boot:run
```

In a second terminal, start the web app:

```sh
cd mvp/web
npm install
npm run dev
```

Open the local URL printed by Vite (normally `http://localhost:5173`). The API
runs at `http://localhost:8080`; its interactive H2 console is at
`http://localhost:8080/h2-console`.

The local H2 database is file-backed at `mvp/api/data/homeaway`, so new places
remain after restarting the API. Sample listings are inserted automatically
when the database is empty.

## MVP features

- Browse lodging, eating, and leisure places near NOVA FCT.
- Search by name, neighborhood, or description; filter by category and monthly
  lodging budget.
- Sort by price, save favorites in the browser, and explore place pins on an
  OpenStreetMap map.
- Add a community place through a validated form.
- Read and create listings through the Java REST API.

## API routes

| Method | Route | Purpose |
| --- | --- | --- |
| `GET` | `/api/services` | Browse, search, and filter places |
| `GET` | `/api/services/{id}` | View one place |
| `POST` | `/api/services` | Create a place |
| `DELETE` | `/api/services/{id}` | Remove a place |

`GET /api/services` accepts optional `q`, `category` (`LODGING`, `EATING`, or
`LEISURE`), and `maxPrice` query parameters.

## Database options

H2 is the out-of-the-box local database for a no-setup demo. To use PostgreSQL,
create a database and run the API with the `postgres` Spring profile:

```sh
cd mvp/api
SPRING_PROFILES_ACTIVE=postgres \
DATABASE_URL=jdbc:postgresql://localhost:5432/homeaway \
DATABASE_USERNAME=homeaway \
DATABASE_PASSWORD=your-local-password \
./mvnw spring-boot:run
```

## Technology

- Java 21, Spring Boot, Spring Data JPA, Bean Validation
- H2 for local development; PostgreSQL profile for deployment
- React, TypeScript, Vite, Leaflet, and OpenStreetMap

## Original coursework

The original Java files remain at the repository root and in `dataStructures/`
and `homeAwayApp/`. They implement the coursework ADTs and command-line
application separately from the web MVP.
