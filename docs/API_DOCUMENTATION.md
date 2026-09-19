# Event Management API Documentation

## Base URL

```text
http://localhost:8080
```

## Overview

The Event Management API is a Spring Boot REST API for creating, managing, searching, filtering, and deleting events.

Each event contains information such as its name, description, schedule, venue, capacity, status, and timestamps.

## Event Object

| Field | Type | Description |
|---|---|---|
| id | Long | Unique event identifier |
| name | String | Event name |
| description | String | Event description |
| startDateTime | LocalDateTime | Event start date and time |
| endDateTime | LocalDateTime | Event end date and time |
| venue | String | Event venue |
| maxCapacity | Integer | Maximum number of participants |
| status | String | Event status |
| createdAt | LocalDateTime | Creation timestamp |
| updatedAt | LocalDateTime | Last update timestamp |

### Event Status

```text
UPCOMING
ONGOING
COMPLETED
CANCELLED
```

## Endpoints

### 1. Create Event

```http
POST /api/events
```

Creates a new event.

#### Request Body

```json
{
  "name": "Java Workshop",
  "description": "Advanced Java and Spring Boot workshop",
  "startDateTime": "2026-10-10T10:00:00",
  "endDateTime": "2026-10-10T13:00:00",
  "venue": "Computer Lab 1",
  "maxCapacity": 60
}
```

#### Response

```text
201 Created
```

The newly created event is returned.

### 2. Get All Events

```http
GET /api/events
```

Returns a paginated list of events.

#### Query Parameters

| Parameter | Required | Default | Description |
|---|---|---|---|
| search | No | — | Search events by name |
| venue | No | — | Filter by venue |
| status | No | — | Filter by event status |
| page | No | 0 | Page number |
| limit | No | 10 | Number of events per page |
| sortBy | No | startDateTime | Field used for sorting |
| direction | No | asc | Sort direction |

#### Examples

Search by name:

```http
GET /api/events?search=Java
```

Filter by venue:

```http
GET /api/events?venue=Auditorium
```

Filter by status:

```http
GET /api/events?status=UPCOMING
```

Pagination:

```http
GET /api/events?page=0&limit=10
```

Sorting:

```http
GET /api/events?sortBy=startDateTime&direction=desc
```

Filters and sorting can be combined.

### 3. Get Event By ID

```http
GET /api/events/{id}
```

Returns a specific event.

#### Example

```http
GET /api/events/5
```

#### Response

```text
200 OK
```

If the event does not exist:

```text
404 Not Found
```

### 4. Update Event

```http
PUT /api/events/{id}
```

Updates an existing event.

Only events with status `UPCOMING` can be updated.

#### Response

```text
200 OK
```

### 5. Cancel Event

```http
PATCH /api/events/{id}/cancel
```

Cancels an event.

An event cannot be cancelled if it is already `CANCELLED` or `COMPLETED`.

#### Response

```text
200 OK
```

### 6. Delete Event

```http
DELETE /api/events/{id}
```

Deletes an event.

Completed events cannot be deleted.

#### Response

```text
204 No Content
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

## Error Responses

### 400 Bad Request

Returned when the request contains invalid data or parameters.

```json
{
  "status": "400",
  "message": "Invalid event data"
}
```

### 404 Not Found

Returned when the requested event does not exist.

```json
{
  "status": "404",
  "message": "Event not found"
}
```

### 409 Conflict

Returned when a business rule prevents the requested operation.

```json
{
  "status": "409",
  "message": "Event is already cancelled"
}
```