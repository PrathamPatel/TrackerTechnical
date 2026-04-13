# SOLUTION

## Overview

This submission implements the required shipment list slice and the optional shipment detail screen (status timeline and key route attributes) using the following:

- Kotlin
- Jetpack Compose
- Retrofit
- Room
- Hilt
- MVI architecture for the list flow; a dedicated ViewModel and UI state for the detail screen

The goal of the solution was to keep the implementation small and easy to review, while still demonstrating clear state handling, offline support, navigation to detail, and a structure that could scale beyond the take-home scope.

## Key design decisions

### 1. Room as the local source of truth

I used Room for persistence so the latest successful shipment list remains visible offline after the first sync. The UI observes cached shipments from Room, while refresh updates the cache from the remote source.

I chose Room over DataStore or a flat file because the data is structured and list-oriented, and would likely evolve toward filtering, sorting, pagination, and optional persistence of detail timelines.

### 2. Repository coordinates remote + local data

The repository abstracts both:

- remote loading from the API
- local observation from Room
- cache replacement after successful GET from the API for the list
- detail: `GET /shipments/{id}` when online; on failure, a fallback built from the cached list row (if present) so the user still sees shipment summary and last known status

This keeps ViewModels focused on UI state rather than data management.

### 3. MVI architecture

The list screen uses an intent/state approach:

- `ShipmentIntent` models user actions
- `ShipmentState` holds UI state
- `ShipmentContentState` prevents invalid UI states by showing only one primary view at a time

This state-driven approach keeps the list predictable and easy to debug. I kept the MVI implementation simple to avoid over-engineering a small project.

The detail screen uses a small sealed `ShipmentDetailUiState` (loading, content, error) and a `ShipmentDetailViewModel` that loads by `shipmentId` from navigation arguments.

### 4. Hosted mock API instead of local asset interceptor

I used a mock REST/JSON endpoint rather than only a local asset-backed interceptor so the app can exercise real connectivity behavior. This makes offline/no-cache handling more realistic while still keeping the backend deterministic and easy to review.

I used [Beeceptor](https://beeceptor.com/) to host the mock API. The app expects **`GET /shipments`** for the list and **`GET /shipments/{id}`** for detail; each path must be configured in Beeceptor (for example, one rule per id).

#### Please note that it does have a limit of 50 requests a day.

### 5. Shipment detail and timeline ordering

Detail responses include a `statuses` array. The mapper sorts events newest first so the UI matches a typical “latest update at the top” timeline.

## How this could scale

### More carriers

We can add new carriers by updating the data mappers, keeping the UI independent of where the data comes from.

### Push updates

- Future versions could add background sync using WorkManager periodic requests, with Room continuing to act as the local source of truth.
- Push-triggered or WebSocket-driven sync could invalidate or refresh cache entries when new tracking events arrive.

### Larger data

For larger data, the solution could evolve to:

- pagination
- partial cache updates instead of full replacement
- Room tables for shipment timelines so full history is available offline after sync
- search/filter/sort queries directly in Room
- timestamp-based APIs to fetch only new or changed data

---

## Trade-offs

To keep the take-home focused, I chose:

- a list plus detail scope (optional detail implemented; search/filter and wide layout left out)
- a lightweight MVI architecture instead of a larger global architecture
- cache replacement on successful list refresh instead of partial merging
- detail timeline not persisted in Room - full history requires a successful detail request or Beeceptor rules for each id; otherwise the user sees cached-summary fallback. Beeceptor on a free version only allows for a max 3 rules. Hence, only shp_1001 and shp_1002 are added to the Mock API.
- focused unit tests rather than broader integration/UI coverage
- Since I went slightly over the 4-hour mark (5.5 hours in total), I skipped the filtering and sorting optional tasks

If this were production code, I would add:

- structured error mapping
- better logging
- last-updated timestamps to track data staleness
- more test coverage across repository and ViewModel state transitions
- Base classes for better abstraction and architecture pattern that would suit the business and product requirements for scalability
- Modularised approach for features
