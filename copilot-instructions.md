# OriGo Batch - Copilot Instructions

## Project Overview

OriGo Batch is a Spring Boot batch application written in Kotlin that synchronizes orienteering organization data from Eventor API systems into a PostgreSQL database. The application runs as a scheduled batch job that fetches, processes, and updates organization and region information.

## Technology Stack

- **Language**: Kotlin 2.2.21
- **Framework**: Spring Boot 3.5.7
- **Build Tool**: Maven
- **Java Version**: 21
- **Database**: PostgreSQL (production), H2 (testing)
- **Key Dependencies**:
  - Spring Data JPA for database operations
  - JAXB for XML processing (Eventor API uses XML)
  - Logstash Logback for structured logging

## Project Structure

```
src/
├── main/
│   ├── kotlin/no/stunor/origo/batch/
│   │   ├── Application.kt           # Main Spring Boot application
│   │   ├── api/                     # External API clients (Eventor)
│   │   ├── data/                    # Repository interfaces (JPA)
│   │   ├── model/                   # Domain entities
│   │   └── services/                # Business logic services
│   └── resources/
│       ├── IOF.xsd                  # XML schema for Eventor API
│       └── application.yml          # Spring configuration
└── test/
    └── kotlin/                      # Test files
```

## Build and Test Commands

### Build
```bash
./mvnw compile
```

### Package
```bash
./mvnw package
```

### Run Tests
```bash
./mvnw test
```

Note: Tests currently have a Java version compatibility issue (compiled with Java 21 but runtime may use Java 17). This is a known existing issue and not a blocker for new changes.

## Coding Standards

### Kotlin Code Style
- Use Kotlin idioms (data classes, null safety, extension functions)
- Prefer `val` over `var` where possible
- Use `lateinit var` for dependency injection with `@Autowired`
- Follow Spring Boot conventions for service and repository layers
- Use SLF4J for logging: `private val log = LoggerFactory.getLogger(this.javaClass)`

### Code Organization
- Keep services focused and single-purpose
- Use `@Service`, `@Repository`, and `@RestController` annotations appropriately
- Domain entities should be in the `model` package
- Repository interfaces extend Spring Data JPA repositories
- External API clients go in the `api` package

### Error Handling
- Catch and log exceptions in batch processing to prevent single failures from stopping the entire job
- Use structured logging with appropriate log levels (info, warn, error)

## Database Considerations

- Entities use UUID as primary keys (generated automatically)
- Use JPA annotations for entity mapping
- Repository interfaces should extend `JpaRepository`
- H2 database is used for testing only
- PostgreSQL is the production database

## Naming Conventions

- Class names: PascalCase (e.g., `BatchService`, `OrganisationRepository`)
- Functions/methods: camelCase (e.g., `updateOrganisations()`)
- Properties: camelCase
- Constants: UPPER_SNAKE_CASE
- Package names: lowercase with dots (e.g., `no.stunor.origo.batch.services`)

## Testing

- Tests are located in `src/test/kotlin`
- Use JUnit 5 for test framework
- Spring Boot Test annotations (`@SpringBootTest`) for integration tests
- Test classes should end with `Test` or `Tests` suffix

## External Dependencies

- **Eventor API**: The application integrates with Eventor systems (orienteering event management) via XML-based APIs
- JAXB is used to generate Java classes from the IOF.xsd schema during build
- API keys are required for Eventor access (stored in database configuration)

## Deployment

- Application is containerized using Docker with a multi-stage build
- Base image: Eclipse Temurin JDK 21 for build, Distroless Java 21 for runtime
- The application runs as a CommandLineRunner and exits after completion
- Set `skip.exit=true` in application properties to keep the application running (useful for testing)

## Important Files

- `pom.xml` - Maven build configuration
- `Dockerfile` - Container image definition
- `src/main/resources/IOF.xsd` - XML schema for Eventor API
- `src/main/kotlin/no/stunor/origo/batch/Application.kt` - Application entry point
- `CODEOWNERS` - Code review assignments
- `CHANGELOG.md` - Release history

## When Making Changes

1. **Run build before and after changes**: Use `./mvnw compile` to verify compilation
2. **Maintain minimal changes**: Only modify what's necessary to address the specific issue
3. **Test your changes**: Run `./mvnw test` (note existing test issues are not your responsibility)
4. **Update documentation**: If changing APIs or adding new features, update relevant documentation
5. **Follow existing patterns**: Look at similar code in the codebase for consistency
6. **Security**: Never commit secrets or API keys in code - they should come from configuration

## Common Tasks

### Adding a New Entity
1. Create entity class in `model/` package with JPA annotations
2. Create repository interface in `data/` package extending `JpaRepository`
3. Create or update service in `services/` package for business logic
4. Add database migration if needed

### Adding a New API Integration
1. Create API client in `api/` package
2. Define XML schema or data models as needed
3. Update services to use the new API client
4. Handle errors appropriately with try-catch and logging

### Updating Dependencies
1. Modify `pom.xml` with new dependency or version
2. Run `./mvnw compile` to ensure compatibility
3. Test the application thoroughly

## Questions or Issues?

- Check `CHANGELOG.md` for recent changes and known issues
- Review existing similar code in the codebase for patterns
- Consult Spring Boot and Kotlin documentation for framework-specific questions
