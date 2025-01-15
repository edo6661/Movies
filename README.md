# Movie App

An Android app that shows movies from The Movie Database (TMDB) API. Built with Kotlin and Jetpack Compose using clean architecture and modern Android development tools.

## Download

You can download the latest version of the app here:
[Download APK](https://github.com/edo6661/submission-android-expert-tmdb/releases/download/v1.0.0/app-debug.apk)

## Screenshots

### App Screens
<div style="display: flex; flex-wrap: wrap; gap: 10px;">

#### Home Screen
<img src="https://firebasestorage.googleapis.com/v0/b/accesstoken-ecommerce-01.appspot.com/o/Movies%2FFavorite_Screen.png?alt=media&token=a2847ff1-58c0-44a3-8698-0a56d77650e8" width="200" alt="Home Screen"/>

#### Detail Screen
<img src="https://firebasestorage.googleapis.com/v0/b/accesstoken-ecommerce-01.appspot.com/o/Movies%2FDetail_Screen.png?alt=media&token=86edb53c-daf1-46af-a860-447ddf66797d" width="200" alt="Detail Screen"/>

#### Favorite Screen
<img src="https://firebasestorage.googleapis.com/v0/b/accesstoken-ecommerce-01.appspot.com/o/Movies%2FFavorite_Screen.png?alt=media&token=a2847ff1-58c0-44a3-8698-0a56d77650e8" width="200" alt="Favorite Screen"/>

#### Search Screen
<img src="https://firebasestorage.googleapis.com/v0/b/accesstoken-ecommerce-01.appspot.com/o/Movies%2FSearch_Screen.png?alt=media&token=289ece42-4aa9-4219-aed5-1231e696cd5a" width="200" alt="Search Screen"/>

#### Settings Screen
<img src="https://firebasestorage.googleapis.com/v0/b/accesstoken-ecommerce-01.appspot.com/o/Movies%2FSettings_Screen.png?alt=media&token=bdd5d07a-beb0-4b9d-bbf8-ac277ab329d7" width="200" alt="Settings Screen"/>

</div>

View more screenshots here:
[Screenshots Gallery](https://drive.google.com/drive/folders/1lNG7YRV6-0IvXEp9ubUpsdc-W517Msd2)

## Features

- Show movie list from TMDB API
- Movie details
- Search Movies
- Save favorite movies using Room Database
- Offline support with caching
- Material Design 3 with dynamic theming
- Swipe to refresh
- Splash screen
- Authentication using Room Database
- Multiple Theme

## Tech Stack

This project uses modern Android development technologies and libraries:

### Android Development
- Minimum SDK 31
- Target SDK 34
- 100% Kotlin
- Jetpack Compose for UI
- Material Design 3
- Clean Architecture with multi-module setup (app, core, domain)
- MVVM

### Libraries
- **UI & Compose**
  - Jetpack Compose with Material 3
  - Navigation Compose
  - Compose Material Icons
  - Lottie Animation
  - Accompanist (SwipeRefresh, SystemUIController)
  - Splash Screen API

- **Dependency Injection**
  - Hilt
  - Hilt Navigation Compose

- **Network & Data**
  - Retrofit2
  - OkHttp3
  - GSON Converter
  - Coil for image loading

- **Local Storage**
  - Room Database
  - DataStore Preferences

- **Testing**
  - JUnit4
  - Espresso
  - Compose UI Testing

## Getting Started

1. Clone this repository
2. Open in Android Studio
3. Sync gradle and wait for build
4. Run the app

### TMDB API Setup
This app uses TMDB API. To run the app:
1. Sign up at [TMDB](https://www.themoviedb.org/)
2. Get your API Access Token
3. Replace `ACCESS_TOKEN` in `build.gradle` with your token

## Architecture

This app uses Clean Architecture with 3 main modules:
- **app**: Android application module with UI and DI
- **domain**: Business logic and use cases
- **core**: Data layer implementation and utilities

## Build Config

```gradle
android {
    compileSdk = 34
    defaultConfig {
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
```

## Contributing

Please make a pull request to contribute or report any issues you find.

## License

```
Copyright (c) 2024 [Your Name]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
