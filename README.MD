# Android Image Gallery App

## Project Overview

This Android application allows users to browse and view images from Pixabay. The app features user authentication (login/register), an image gallery with pagination, and detailed image information views.

### Note on Authentication

The login and registration functionality is implemented with local mocks in the data layer for demonstration purposes. Session management and persistent authentication are intentionally omitted. In a production environment, this would typically include:

- Secure token storage
- Session management
- Token refresh mechanisms
- Secure credential storage

The current implementation focuses on demonstrating the UI flow and form validation rather than full authentication infrastructure.

## Tech Stack & Architecture

This Android application is built using modern Android development practices and libraries:

- **Language**: Kotlin
- **Architecture Pattern**: MVVM with Clean Architecture
- **UI Framework**: XML layouts with Data Binding
- **Core Libraries**:
  - Coroutines & Flows for async operations
  - Hilt for dependency injection
  - Retrofit for networking
  - Room for local storage
  - Navigation Component with SafeArgs
  - Coil for image loading
  - Paging3 for list pagination
- **Build System**: Gradle with Version Catalogs
- **Minimum SDK**: API 26

## Architecture

The application follows Clean Architecture principles with a multi-module structure:

### Module Structure

- **app**: Main application module containing UI components and navigation
- **core-domain**: Business logic and use cases
- **core-data**: Repository implementations and data sources
- **core-network**: API service and network models
- **core-database**: Local storage using Room
- **core-testing**: Testing utilities and shared test resources
- **core-model**: This is omitted but in production the model which we expose to the viewModel should be mapped and be here.

### Data Flow

1. UI Layer (Fragments/ViewModels) → Use Cases → Repositories
2. Repositories coordinate between network and local storage
3. Single source of truth maintained in local database
4. Offline-first approach pager3

## Testing Strategy

The project implements a focused testing strategy:

### UI Testing

- **Framework**: Espresso
- **Scope**: Critical user flows including:
  - Login form validation
  - Registration form validation
  - Navigation between screens
  - Image list display and interaction
  - Details screen content verification

### Business Logic Testing

- **Framework**: JUnit
- **Approach**: Unit tests with fake dependencies
- **Location**: Core domain module
- **Implementation**:
  - Uses fake repositories and data sources
  - Runs on JVM for fast execution
  - No mocking libraries - uses simple fake implementations
  - Tests business rules and use case logic in isolation

### Test Coverage Focus

- Form validation logic
- User authentication flows
- Image data transformation
- Error scenarios

### Intentionally Omitted

- Screenshot tests
- Network integration tests
- Database integration tests
- End-to-end tests
- Paging3 integration tests (including RemoteMediator)

## Code Style & Formatting

This project uses Spotless for automated code formatting and style enforcement. To apply Spotless formatting to all files, run:

```shell
./gradlew --init-script gradle/init.gradle.kts spotlessApply
```

This will:

- Format all Kotlin files according to ktlint rules
- Format Kotlin script files (.kts)
- Format XML files
- Ensure consistent code style across the project
