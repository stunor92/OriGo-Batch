# OriGo-Batch

A Spring Boot batch application written in Kotlin that synchronizes organization data from Eventor API systems into a PostgreSQL database.

## Overview

OriGo-Batch is a command-line batch processing application that fetches organization and region data from Eventor (orienteering event management system) instances and stores them in a local database. The application is designed to run as a scheduled job or on-demand task.

## Technologies

- **Language**: Kotlin 2.2.21 / Java 21
- **Framework**: Spring Boot 3.5.7
- **Database**: PostgreSQL (with H2 for testing)
- **Build Tool**: Maven
- **Container**: Docker with distroless image

## Features

- Fetches organization data from multiple Eventor instances via REST API
- Synchronizes regions and organizations into PostgreSQL database
- Uses JAXB for XML data processing (IOF schema)
- Containerized deployment with Docker
- Automatic shutdown after batch completion
- Structured logging with Logstash encoder

## Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- PostgreSQL database
- Docker (optional, for containerized deployment)

## Installation

### Clone the Repository

```bash
git clone https://github.com/stunor92/OriGo-Batch.git
cd OriGo-Batch
```

### Build the Application

Using Maven wrapper:

```bash
./mvnw clean package
```

Or with Maven installed:

```bash
mvn clean package
```

## Configuration

The application requires the following environment variables to be set:

### Database Configuration

- `POSTGRES_DB`: PostgreSQL database connection URL (e.g., `jdbc:postgresql://localhost:5432/origo`)
- `POSTGRES_USER`: Database username
- `POSTGRES_PASSWORD`: Database password

### Optional Configuration

- `skip.exit`: Set to `true` to prevent automatic application shutdown after batch completion (default: `false`)

### Configuration File

You can also create a `.env` file in the project root with the following properties:

```properties
POSTGRES_DB=jdbc:postgresql://localhost:5432/origo
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password
```

### Application Profiles

- **default**: Uses PostgreSQL database (requires environment variables)
- **local**: Development profile (see `application-local.yml`)

## Usage

### Run Locally

```bash
java -jar target/origo-batch-3.0.1-SNAPSHOT.jar
```

Or with Maven:

```bash
./mvnw spring-boot:run
```

### Run with Docker

Build the Docker image:

```bash
docker build -t origo-batch .
```

Run the container:

```bash
docker run --rm \
  -e POSTGRES_DB=jdbc:postgresql://host.docker.internal:5432/origo \
  -e POSTGRES_USER=your_username \
  -e POSTGRES_PASSWORD=your_password \
  origo-batch
```

## Project Structure

```
OriGo-Batch/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── no/stunor/origo/batch/
│   │   │       ├── api/           # Eventor API client
│   │   │       ├── data/          # Repository layer
│   │   │       ├── model/         # Domain models
│   │   │       ├── services/      # Business logic
│   │   │       └── Application.kt # Main application entry point
│   │   └── resources/
│   │       ├── IOF.xsd           # IOF XML schema definition
│   │       ├── application.yml    # Main configuration
│   │       └── application-local.yml
│   └── test/
│       └── kotlin/               # Test sources
├── Dockerfile                    # Docker build configuration
├── pom.xml                      # Maven project configuration
└── README.md                    # This file
```

## Development

### Running Tests

```bash
./mvnw test
```

### Building Without Tests

```bash
./mvnw package -DskipTests
```

### Code Style

The project uses Kotlin coding conventions. Ensure your IDE is configured to follow Kotlin style guidelines.

## Database Schema

The application manages the following main entities:

- **Eventor**: Configuration for Eventor instances (base URL, API keys)
- **Region**: Geographic regions from Eventor
- **Organisation**: Orienteering clubs and organizations

The application uses JPA/Hibernate with PostgreSQL dialect for database operations.

## How It Works

1. Application starts as a CommandLineRunner
2. Fetches all configured Eventor instances from the database
3. For each Eventor instance:
   - Retrieves organization data via REST API
   - Updates regions in the database
   - Updates organizations in the database
4. Logs completion status
5. Automatically shuts down (unless `skip.exit=true`)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Versioning

This project uses semantic versioning and automated releases via [Release Please](https://github.com/googleapis/release-please).

See [CHANGELOG.md](CHANGELOG.md) for version history.

## License

This project is maintained by [stunor92](https://github.com/stunor92).

## Links

- [GitHub Repository](https://github.com/stunor92/OriGo-Batch)
- [Issue Tracker](https://github.com/stunor92/OriGo-Batch/issues)
- [Changelog](CHANGELOG.md)
