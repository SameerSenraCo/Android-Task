# Acharya Prashant Org Task

## Features
- Displays a grid of media coverages with images and details.
- Caches images for improved performance using memory and disk caching.
- Handles network errors gracefully.
- Implements a clean architecture with a separation of concerns.

## Tech Stack
This project is built using the following technologies:

- **Kotlin**: The primary programming language for Android development.
- **Android Jetpack**:
  - **ViewModel**: To manage UI-related data lifecycle-consciously.
  - **LiveData**: To observe data changes in a lifecycle-aware manner.
- **Dagger Hilt**: For dependency injection, providing a more modular and testable codebase.
- **Retrofit**: For networking, enabling seamless API calls.
- **Gson**: For JSON serialization/deserialization when interacting with the API.
- **RecyclerView**: For displaying lists of data in a performant way.
- **Coroutines**: For managing asynchronous tasks, improving performance and readability.
- **OkHttp**: As the HTTP client used by Retrofit to handle network requests.

## Architecture
The application follows the MVVM (Model-View-ViewModel) architecture, ensuring a clean separation of concerns:

- **Model**: Represents the data structure and business logic (e.g., `MediaCoverage`, `Thumbnail`, `BackupDetails`).
- **View**: UI components (e.g., `Activity`, `Fragment`, `Adapter`) that display the data.
- **ViewModel**: Acts as a bridge between the Model and the View, managing UI-related data and logic.

## Installation
To get started with the project:

1. Clone the repository:
   ```bash
   git clone https://github.com/SameerSenraCo/Android-Task.git
2. Sync the project with Gradle files.
3. Run the application on an emulator or a physical device.

## Gradle Configuration
The project is configured using Gradle with the following specifications:

- Compile SDK Version: 34
- Minimum SDK Version: 26
- Target SDK Version: 34
- Version Code: 1
- Version Name: 1.0
- Java Version: 11
- Kotlin JVM Target: 11
