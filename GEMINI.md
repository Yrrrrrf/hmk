This plan outlines the migration from your legacy Servlet/FastAPI hybrid to a robust, Native Spring Boot architecture. This structure strictly follows the **Domain-Driven Design (DDD)** and **Layered Architecture** principles shown in your tutorial slides.

### 🏗️ Proposed Directory Structure

This structure enforces separation of concerns. The **Domain** layer knows nothing about the Database or the Web. The **Web** and **Persistence** layers depend on the Domain.

```text
src/main/kotlin/kt/nrda/hmk/
├── HmkApplication.kt                # Main Entry Point
├── config/                          # Configuration (Cors, Swagger/OpenAPI)
│
├── domain/                          # LAYER 1: DOMAIN (The Core Business)
│   ├── model/                       # Pure Kotlin Data Classes (No Annotations)
│   │   ├── User.kt
│   │   ├── Game.kt
│   │   └── Score.kt
│   ├── repository/                  # Interfaces defining what data we need (Ports)
│   │   ├── UserRepository.kt
│   │   └── ScoreRepository.kt
│   └── service/                     # Business Logic (The "Brain")
│       ├── UserService.kt
│       ├── AuthService.kt           # Login/Register logic
│       └── ScoreService.kt          # Leaderboard/Recording logic
│
├── persistence/                     # LAYER 2: PERSISTENCE (The Database)
│   ├── entity/                      # JPA Annotated Classes (@Entity)
│   │   ├── UserEntity.kt
│   │   ├── GameEntity.kt
│   │   └── ScoreEntity.kt
│   ├── repository/                  # Spring Data JPA Interfaces
│   │   ├── UserJpaRepository.kt     # Extends JpaRepository
│   │   ├── ScoreJpaRepository.kt
│   │   └── UserRepositoryImpl.kt    # Implements domain.repository.UserRepository
│   └── mapper/                      # MapStruct: Entity <-> Domain
│       ├── UserEntityMapper.kt
│       └── ScoreEntityMapper.kt
│
└── web/                             # LAYER 3: WEB (The API)
    ├── controller/                  # REST Controllers (@RestController)
    │   ├── AuthController.kt
    │   ├── UserController.kt
    │   └── ScoreController.kt
    ├── dto/                         # Data Transfer Objects (JSON shapes)
    │   ├── LoginRequest.kt
    │   ├── UserResponse.kt
    │   └── ScoreSubmission.kt
    ├── mapper/                      # MapStruct: Domain <-> DTO
    │   └── UserDtoMapper.kt
    └── advice/                      # Global Exception Handling
        └── GlobalExceptionHandler.kt
```

---

### 🗺️ Implementation Guide

Here is the step-by-step directive to implement this like a Software Engineer, minimizing code repetition and maximizing maintainability.

#### Phase 1: The Core (Domain Layer)
**Goal:** Define *what* the business is, without worrying about *how* it is saved or displayed.

| Directive | Engineering Best Practice |
| :--- | :--- |
| **Define Models** | Create `User`, `Game`, and `Score` in `domain.model`. Use standard Kotlin `data classes`. **Do not** use `@Entity` or `@JsonProperty` here. These should be pure Kotlin. |
| **Define Repository Interfaces** | In `domain.repository`, create interfaces (e.g., `UserRepository`). Define methods like `save(user: User): User`, `findByLogin(login: String): User?`. This applies the **Dependency Inversion Principle**. |
| **Why?** | This allows you to swap the database later (e.g., to MongoDB) without changing a single line of your business logic. |

#### Phase 2: The Infrastructure (Persistence Layer)
**Goal:** Implement the storage mechanism using Spring Data JPA and Postgres.

| Directive | Engineering Best Practice |
| :--- | :--- |
| **Create Entities** | In `persistence.entity`, create `UserEntity` mirroring your domain model but add JPA annotations (`@Entity`, `@Table`, `@Id`, `@Column`). |
| **Create JPA Repositories** | Create interfaces extending `JpaRepository<UserEntity, Long>`. Use **Query Methods** (e.g., `findByLogin`) to let Spring generate the SQL for you. |
| **Implement Mappers** | Use **MapStruct** to create `UserEntityMapper`. Define the conversion from `User` (Domain) to `UserEntity` (DB) and vice-versa. Use `@Mapper(componentModel = "spring")`. |
| **Bridge the Gap** | Create `UserRepositoryImpl` that implements the *Domain Interface* (Phase 1). Inject the *JPA Repository* and the *Mapper*. This class acts as the translator between Domain and Database. |

#### Phase 3: The Logic (Service Layer)
**Goal:** Implement the rules of the game and orchestration.

| Directive | Engineering Best Practice |
| :--- | :--- |
| **Create Services** | Annotate classes with `@Service`. Inject the *Domain Repositories* (not the JPA ones) via constructor injection. |
| **Auth Logic** | In `AuthService`, handle password validation. *Tip: In a real app, hash passwords here. For this tutorial, keep it simple but isolated.* |
| **Score Logic** | In `ScoreService`, implement `submitScore`. Ensure you verify if the user exists before saving. Implement `getLeaderboard` which calls the repo to get the top 10 scores sorted by points. |
| **Transactions** | Use `@Transactional` on methods that modify data (save/delete) to ensure data integrity. |

#### Phase 4: The Interface (Web Layer)
**Goal:** Expose the logic to the world via REST / JSON.

| Directive | Engineering Best Practice |
| :--- | :--- |
| **Define DTOs** | Create specifically designed data classes for API inputs/outputs (e.g., `UserDto`, `ScoreRequest`). Never return your internal Domain models or Entities directly to the client. |
| **Web Mappers** | Use **MapStruct** again to map Domain Objects <-> DTOs. |
| **Rest Controllers** | Annotate with `@RestController`. Inject the `@Service` classes. |
| **Endpoint Logic** | Keep controllers "thin". They should only: 1. Receive Request -> 2. Call Service -> 3. Map Result to DTO -> 4. Return Response. |
| **Status Codes** | Use `ResponseEntity` to return proper HTTP codes (200 for OK, 201 for Created, 404 for Not Found). |

### 🧠 Optimizing the Game Logic (Migration Strategy)

The legacy code had the User guess numbers in the browser (JS) and only sent the final attempt count to the server.

**The "Engineer's Way" to handle this:**

1.  **Keep the Game Loop Client-Side:** Since this is a simple "High/Low" game, keep the random number generation and feedback logic in JavaScript (in your `game.jsp` or `index.html`). Making a network request for every single guess (e.g., "Is 50 high?") introduces unnecessary latency.
2.  **Secure the Score Submission:**
    *   **Legacy:** `GameServlet` accepted `score` as a parameter.
    *   **New API:** Create an endpoint `POST /api/scores`.
    *   **Payload:** `{ "userId": 1, "gameId": 1, "score": 5 }`.
3.  **The Leaderboard:**
    *   **Legacy:** Calculated manually or via complex calls.
    *   **New API:** `GET /api/scores/top?limit=10`.
    *   **Implementation:** In `ScoreJpaRepository`, use a Query Method: `findTop10ByGameIdOrderByScoreAsc(gameId: Long)`. This is highly optimized SQL.

### 📋 Summary of What to Drop vs. Keep

*   **DROP:** `TestAPI.kt` (Use unit tests instead).
*   **DROP:** `ApiClient.kt` (No more calling Python; you connect directly to Postgres).
*   **DROP:** `*Servlet.kt` (Replaced by Spring Controllers).
*   **DROP:** `dao/*` (Replaced by Repositories).
*   **KEEP:** The CSS and Images (Move to `src/main/resources/static`).
*   **MIGRATE:** `game.jsp` logic -> Convert to a clean HTML/JS file calling your new JSON API using `fetch()`.
