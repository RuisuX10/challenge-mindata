# Hotel Availability Search API

Spring Boot API for creating hotel availability searches and counting searches with matching criteria. Search events are published to Kafka and persisted by a separate Kafka consumer in PostgreSQL.

## Requirements

- Docker with the Docker Compose plugin

The Docker build compiles the application with Java 21 inside a build container. Java and Maven do not need to be installed on the host.

## Start the application

From the project root, build the application image and start the API, PostgreSQL, and Kafka:

```bash
docker compose up --build -d
```

Follow the application logs until Spring Boot is ready:

```bash
docker compose logs -f app
```

The API listens on `http://localhost:8080`. Compose waits for PostgreSQL and Kafka health checks before starting the application.

Stop the infrastructure when finished:

```bash
docker compose down
```

To remove the database and Kafka data volumes as well:

```bash
docker compose down -v
```

## API documentation

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

## Endpoints

### `POST /search`

Creates a search and returns its generated ID. Dates use `dd/MM/yyyy`; `checkIn` must be before `checkOut`, and ages must be zero or greater. The order of ages is significant when matching searches.

Example request:

```json
{
  "hotelId": "1234aBc",
  "checkIn": "29/12/2026",
  "checkOut": "31/12/2026",
  "ages": [30, 29, 1, 3]
}
```

Successful response (`202 Accepted`):

```json
{
  "searchId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### `GET /count?searchId={searchId}`

Returns the stored search for that ID and the number of stored searches with the same hotel, dates, and ordered ages. A search may briefly return `404` after creation while its Kafka event is waiting to be persisted.

Successful response (`200 OK`):

```json
{
  "searchId": "550e8400-e29b-41d4-a716-446655440000",
  "search": {
    "hotelId": "1234aBc",
    "checkIn": "29/12/2026",
    "checkOut": "31/12/2026",
    "ages": [30, 29, 1, 3]
  },
  "count": 1
}
```

Invalid request data returns `400 Bad Request`; an unknown `searchId` returns `404 Not Found`.

## Tests and coverage

The image build compiles the application but skips tests. To run tests and generate the JaCoCo report, use Java 21 and the Maven wrapper:

```bash
./mvnw clean verify
```

After the build, open `target/site/jacoco/index.html` to view the coverage report.

The project aims to maintain at least 80% coverage for instructions, branches, lines, and methods, as required by the technical challenge.

Running the application with Docker Compose does not require Java or Maven to be installed on the host.
