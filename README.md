# Event Management System

A Spring Boot REST API for managing events with CRUD operations, validation, searching, filtering, pagination, sorting, and business-rule enforcement.

## Features

- Create, read, update, and delete events
- Event validation
- Search events by name
- Filter events by venue and status
- Pagination
- Sorting by event fields
- Event cancellation
- Venue conflict detection
- Consistent error handling
- MySQL database persistence
- RESTful API design

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Jakarta Bean Validation
- Postman

## Project Structure

```text
src/main/java/com/example/eventmanagement
│
├── controller
├── service
├── repository
├── entity
├── dto
├── enums
└── exception
```

## Database

The application uses MySQL for persistent data storage.

Create the database using:

```sql
CREATE DATABASE event_management_db;
```

Database credentials are provided through environment variables:

```text
DB_USERNAME
DB_PASSWORD
```

The application configuration is located in:

```text
src/main/resources/application.properties
```

## Running the Application

### Prerequisites

Make sure the following are installed:

- Java 21
- Maven
- MySQL

### Steps

1. Clone the repository.

2. Create the MySQL database:

```sql
CREATE DATABASE event_management_db;
```

3. Configure the following environment variables:

```text
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

4. Start the Spring Boot application.

The API will be available at:

```text
http://localhost:8080
```

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/events` | Create an event |
| GET | `/api/events` | Get events with search, filtering, pagination and sorting |
| GET | `/api/events/{id}` | Get an event by ID |
| PUT | `/api/events/{id}` | Update an event |
| PATCH | `/api/events/{id}/cancel` | Cancel an event |
| DELETE | `/api/events/{id}` | Delete an event |

## Event Status

Events can have the following statuses:

```text
UPCOMING
ONGOING
COMPLETED
CANCELLED
```

Newly created events start with the `UPCOMING` status.

## Search, Filtering, Pagination and Sorting

The `GET /api/events` endpoint supports:

- Search by event name
- Filtering by venue
- Filtering by status
- Pagination
- Sorting
- Combining multiple query parameters

Example:

```text
GET /api/events?venue=Auditorium&status=UPCOMING&sortBy=startDateTime&direction=desc
```

## Validation Rules

- Event name is required.
- Venue is required.
- Start date and time must be in the future.
- End date and time must be in the future.
- End date and time must be after the start date and time.
- Maximum capacity must be greater than zero.

## Business Rules

- Two active events cannot overlap at the same venue.
- Back-to-back events at the same venue are allowed.
- Cancelled and completed events do not block venue availability.
- Only upcoming events can be updated.
- Completed events cannot be deleted.
- Completed events cannot be cancelled.
- Cancelled events cannot be cancelled again.

## Error Handling

The API provides consistent responses for common errors.

### 400 Bad Request

Used for invalid request data or parameters.

```json
{
  "status": "400",
  "message": "Invalid event data"
}
```

### 404 Not Found

Used when an event does not exist.

```json
{
  "status": "404",
  "message": "Event not found"
}
```

### 409 Conflict

Used when a business rule prevents an operation.

```json
{
  "status": "409",
  "message": "Event is already cancelled"
}
```

## API Documentation

Detailed API documentation is available in:

```text
docs/API_DOCUMENTATION.md
```

## Testing

The API was tested using Postman, including:

- Successful CRUD operations
- Validation errors
- Invalid pagination parameters
- Invalid sorting parameters
- Invalid status values
- Venue conflicts
- Cancellation rules
- Deletion rules
- Search and filtering
- Pagination
- Sorting
- Combined query parameters

## Postman Collection

The project includes a Postman collection containing the main API requests and edge-case tests.

## Author
 
Archit Singhal 

Developed as a backend development project using Spring Boot and MySQL.