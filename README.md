# Discogs Challenge API

## Description
A REST API that interacts with the Discogs API to search for and compare musical artists, allowing for the storage and comparison of their discographies. A challenge for Clara.

## Requirements
- **Docker** and **Docker Compose**
- **Java 17** (optional, if not using the wrapper)

## Main Technologies
- **Spring Boot 3.3.7**
- **MySQL 8.0** (Docker image)
- **OpenAPI (Swagger UI)**
- **JUnit 5 & Mockito**
- **Gradle** (includes wrapper)

## Build and Run

### Using Gradle Wrapper
**Build the project and execute all reports:**
```bash
./gradlew build
```
This command will execute:
- Unit and integration tests
- Code analysis with Checkstyle
- Security analysis with SpotBugs
- Coverage report with Jacoco

### Run the Application
**Start the application and the database:**
```bash
docker-compose up
```
> *Note:* The `docker-compose.yml` file configures a MySQL database instance for the application.

### API Documentation
The complete API documentation is available through Swagger UI once the application is running:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints

### Logical Execution Order of Endpoints:

1. **Search Artists (Public)**  
   **GET /api/artists/search?q={artist_name}**  
   Searches for artists in Discogs by name.

2. **Save Discography (Protected)**  
   **POST /api/discography/save?artistId={discogs_artist_id}**  
   Saves the artist's discography to the local database.

3. **Get Discography (Protected)**  
   **GET /api/discography/{id}**  
   Retrieves the saved discography of an artist.

4. **Compare Artists (Protected)**  
   **GET /api/comparison?artistIds={id1,id2}**  
   Compares the discographies of the saved artists.

> *Note:* The API requires authentication for protected endpoints.

## Security
The application implements **basic authentication** for protected endpoints.  
Default credentials are configured in the `application.properties` file:
- **Username:** admin
- **Password:** admin

These credentials are required when accessing the API via Swagger UI or when making requests through tools like Postman.

## Tests
The project includes tests that verify:
- Integration with the Discogs API
- Business logic for artist comparison
- Security and authentication
- Data persistence

## Run all reports only (Checkstyle, SpotBugs, Jacoco):

```bash
./gradlew allReports
```

## Project Structure
The project follows a **hexagonal architecture** (ports and adapters) to maintain a clear separation of responsibilities and facilitate testing.

## Additional Information
- **Discogs API Tokens:** The tokens used to access the Discogs API are stored in the `application.properties` file.
- Tokens remain valid indefinitely unless manually revoked through the Discogs account settings.  
