# Contact, Task, and Appointment REST API (Enhanced)

Enhanced version of my CS 320 Project One for the CS 499 Capstone at Southern New Hampshire University (Milestone Two, Software Design and Engineering). The original three in-memory Java services are rebuilt as a layered Spring Boot REST API with validation, consistent error responses, and JWT-based security.

## Overview

The same Contact, Task, and Appointment services from CS 320, now exposed as a REST API that a mobile or web client could actually call. Each resource has a controller that handles HTTP requests, request objects (DTOs) that validate input, a service that holds the business logic, and a repository that stores records. Users register and log in to get a token, and they can only see and change their own records.

| Service | Field rules (same as CS 320) |
|---|---|
| **Contact** | ID up to 10 characters (required, not updatable), first and last name up to 10 characters, phone exactly 10 digits, address up to 30 characters |
| **Task** | ID up to 10 characters (required, not updatable), name up to 20 characters, description up to 50 characters |
| **Appointment** | ID up to 10 characters (required, not updatable), date cannot be in the past, description up to 50 characters |

Each service supports creating with a unique ID, listing, getting by ID, partial updates of the allowed fields, and deleting by ID.

## How the original limitations were addressed

| Limitation in CS 320 version | Enhancement |
|---|---|
| Services only existed as classes called by unit tests | Exposed as REST endpoints that any client can call, testable in Swagger UI or Postman |
| All three services repeated the same add, delete, update, and lookup logic | One generic `InMemoryRepository<T>` and `AbstractOwnedService<T>` shared by all three |
| Validation errors were thrown as `IllegalArgumentException` | Bean Validation on request DTOs, with JSON error responses (Problem Details) that list each invalid field and use the correct HTTP status code |
| `HashMap` was not thread-safe | `ConcurrentHashMap`, with an atomic `putIfAbsent` so two requests with the same ID cannot both be saved |
| `Appointment` used the outdated `java.util.Date` | `java.time.LocalDateTime`, which is immutable and closes the bug where a saved date could be changed to a past date |
| `Task` and `Appointment` accepted an empty string as an ID | All IDs and required fields now reject empty and blank values |
| No authentication | JWT login, BCrypt password hashing, USER and ADMIN roles, and ownership checks on every record |
| Folder structure did not match the package and there was no build file | Standard Maven project layout with a `pom.xml` |

Validation limits that were repeated as magic numbers are now kept in one `ValidationRules` class.

## Project structure

```
src/main/java/projectone/enhanced/
  Cs320Application.java   Spring Boot entry point
  common/                 generic repository, base service, validation rules, error handling, Swagger config
  contact/                Contact model, DTOs, repository, service, controller
  task/                   same structure as contact
  appointment/            same structure as contact
  auth/                   user accounts, roles, register and login, admin endpoint
  security/               JWT signing and verification, security filter chain
src/main/resources/
  application.properties  settings (secrets are read from environment variables)
src/test/java/projectone/enhanced/
  ApiIntegrationTest.java full request tests through the security layer
  common/                 repository and validation boundary tests
  contact/, task/, appointment/   service tests, including ownership checks
```

## Requirements

- Java 21 (JDK)
- Maven 3.9 or later, or Eclipse with Maven support (m2e)

## Configuration

No secrets are stored in the code. They are read from environment variables when the app starts.

| Variable | Purpose |
|---|---|
| `JWT_SECRET` | Key used to sign login tokens, at least 32 characters. If it is not set, a random key is generated and tokens stop working after a restart. |
| `ADMIN_USERNAME` and `ADMIN_PASSWORD` | Optional. Creates an ADMIN account at startup. If either is missing, no admin account is created. |

## Running the tests

### In Eclipse

Right-click the `cs320-enhanced` project and select **Run As > Maven test**. The console should end with `BUILD SUCCESS`.

### In a terminal

From the `cs320-enhanced` folder:

```
mvn test
```

All 47 tests should pass. The code coverage report is saved to `target/site/jacoco/index.html`.

## Running the API

In PowerShell, from the `cs320-enhanced` folder:

```
$env:JWT_SECRET = "replace-with-a-long-random-string-32-plus-chars"
$env:ADMIN_USERNAME = "admin"
$env:ADMIN_PASSWORD = "ChooseAStrongOne123"
mvn spring-boot:run
```

Then open **http://localhost:8080/swagger-ui.html**. Register a user with `/api/auth/register`, log in with `/api/auth/login`, copy the `accessToken`, click **Authorize**, and paste it in. Every other endpoint can then be tried from the page.

## Endpoints

All contact, task, and appointment endpoints require the header `Authorization: Bearer <token>`.

| Method | Path | Notes |
|---|---|---|
| POST | `/api/auth/register` | Creates a USER account |
| POST | `/api/auth/login` | Returns a token that expires after 60 minutes |
| GET | `/api/contacts` | Your contacts (admins see all) |
| GET | `/api/contacts/{id}` | Returns 404 if it does not exist or belongs to someone else |
| POST | `/api/contacts` | 201 on success, 400 for invalid fields, 409 for a duplicate ID |
| PATCH | `/api/contacts/{id}` | Updates only the fields that are sent |
| DELETE | `/api/contacts/{id}` | 204 on success |
| GET | `/api/admin/users` | ADMIN only |

`/api/tasks` and `/api/appointments` follow the same pattern.

Example request bodies:

```json
{ "contactId": "C1", "firstName": "Josh", "lastName": "Hall", "phone": "6035551234", "address": "1 Main St" }
{ "taskId": "T1", "name": "Milestone Two", "description": "Enhance CS 320" }
{ "appointmentId": "A1", "appointmentDate": "2027-01-15T14:30:00", "description": "Dentist" }
```

## Known limitations

- Data is still stored in memory, so it is lost when the app stops. The repository layer is separate from the services, so it could be swapped for a database without changing the controllers or services.
- There is no rate limiting on the login endpoint.