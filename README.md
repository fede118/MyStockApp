# MyStocks - Android Stock Watchlist Application

**MyStocks** is an Android application designed to allow users to track and view information about individual stocks. It provides a clean and intuitive interface for accessing stock data.

## Overview

This application is built using modern Android development practices and leverages the power of Jetpack Compose for a declarative UI. It allows users to:

*   View a list of stocks that the user is following (AKA. Watchlist)
*   Navigate to a detailed view for a specific stock, showing more in-depth information.

## How it Looks (WIP - making changes everyday now)

Watchlist Screen | Single Stock Screen
------|-------
![image](https://github.com/user-attachments/assets/d9f94f67-8b3b-439b-b0cf-545e1f52aacb) | ![image](https://github.com/user-attachments/assets/52b61004-06a6-4d00-bdca-dad65f56f3ab)

### Last Update (still in development)

https://github.com/user-attachments/assets/3d330df0-d389-4bf8-9076-354e697e6c13

## Key Features

*   **Jetpack Compose:** The user interface is built using Jetpack Compose, allowing for a more reactive and efficient UI development process.
*   **Navigation:** The app utilizes the Jetpack Navigation library to manage navigation between the home screen and the single stock detail screen.
*   **Dependency Injection:** Hilt is used for dependency injection, making the code more modular, testable, and maintainable.
*   **MVVM:** The app uses ViewModels to manage UI-related data and lifecycle, ensuring data consistency and separation of concerns.

## Technologies Used

*   **Kotlin:** The primary programming language for the application.
*   **Jetpack Compose:** For building the user interface in a declarative way.
*   **Jetpack Navigation:** For managing in-app navigation between different screens.
*   **Hilt:** For dependency injection, simplifying the management of dependencies.
*   **ViewModel:** For managing UI-related data and lifecycle.
*   **Room:** For local data persistence, allowing the app to store and retrieve stock data locally.
*   **Retrofit:** For making network requests to fetch stock data from remote APIs.
*   **OkHttp:** As the underlying HTTP client for Retrofit, providing efficient network communication.
*   **Mockito:** For creating mock objects in unit tests, enabling isolated testing of components.
*   **Kotlin DSL:** For defining dependencies and configurations in the Gradle build files, providing a more concise and type-safe way to manage the build process.
*   **Version Catalog (TOML):** For centralizing dependency versions in a `libs.versions.toml` file, ensuring consistency and simplifying dependency updates.
*   **SerpApi:** The API used to retrieve stock information. https://serpapi.com/

## CI/CD and Quality Assurance

This project incorporates Continuous Integration/Continuous Deployment (CI/CD) practices and quality assurance measures to ensure code quality and stability:

*   **JaCoCo:** Used for generating code coverage reports, helping to identify areas of the code that are not adequately tested.
*   **Test Suite:** A comprehensive suite of unit and UI tests is being developed to verify the correctness of the application's logic and UI.
*   **Code Coverage:** Code coverage metrics are used to track the percentage of code covered by tests, ensuring that a high proportion of the codebase is tested.
*   **Detekt:** Static code analysis tool used to identify potential code smells, bugs, and style violations, improving code quality and maintainability.
*   **Todo: Lint:** Static code analysis tool used to identify potential code smells, bugs, and style violations, improving code quality and maintainability. Currently lint is failing because of Compose and functions Starting with capital letters

## Architecture

The application follows a clean architecture, separating concerns into distinct layers:

*   **UI Layer:** Composable functions and ViewModels handle the user interface and its data. This layer is responsible for displaying information to the user and handling user interactions.
*   **Domain Layer:** This layer contains the business logic of the application. It defines use cases and entities related to stock data. Responsible for the core business rules of the application.
*   **Data Layer:** Responsible for fetching and managing stock data. It handles interactions with data sources, such as local databases (Room) or remote APIs (Retrofit/OkHttp).

## Future Enhancements

*   **Match Watchlist screen to single stock screen:** I want to have a more consistent UI maybe have the same card design but smaller with less info, and see the second screen on tap
*   **Stock Search and Add to Watchlist:** Implement a search functionality (through and api) to allow users to find stocks and add them to their watchlist.
*   **Error Handling:** Implement robust error handling for network requests, data parsing, and other potential issues, providing informative feedback to the user.
*   **UI Compose Testing:** Address the current issues with running Compose tests to ensure the UI is thoroughly tested.

## Contact

If you have any questions or suggestions, feel free to reach out!

## Current backlog
This section has things that I want to work on, improve or that I know are not the best but are not a priority

* ** Make the watchlist UI match the single stock UI. I want each stock to be a smaller version of the single stock UI, maybe make it larger on tap (hiding all the other stuff)
* ** In the single stock screen get the labels for the Y axis. This is tricky since I added a padding so that the min and max values are not at the edge of the graph, but this means that getting the data sorted from max to min and then getting the normalized Y index wont work
* ** Find a better way to mock information for previews (also to stop detekt triggering false positives) 
* ** Fix @DarkAndLightAnnotation detekt issue where the name doesn't match the file name and suppress is not working

---
