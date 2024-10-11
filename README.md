## Overview
Post App is a simple Android application that allows users to create, update, and view posts. Each post contains a title, message, and optionally an image. The app interacts with a backend server through API calls, using Retrofit for network operations, and follows modern Android development practices such as MVVM architecture and Kotlin Coroutines.

## Features
- **Create a Post**: Users can create a new post by providing a title, a message, and optionally an image from their device.
- **Update a Post**: Users can update an existing post, modify the title, message, or update the image.
- **View Posts**: Users can view a list of all posts.
- **Upload Images**: Images are uploaded with posts as multipart file data.

## Technology Stack
- **Programming Language**: Kotlin
- **UI**: XML Layout
- **Network**: Retrofit with Multipart form data for image upload
- **Dependency Injection**: Dagger Hilt
- **Concurrency**: Kotlin Coroutines
- **Architecture**: MVVM (Model-View-ViewModel)

## Prerequisites
### Before running the project, ensure you have the following:

- **Android Studio**: Version 4.0 or above.
- **Minimum SDK**: 21 (Android 5.0 Lollipop)
- **Gradle**: Latest version.
- **Backend API**: A running backend server with endpoints for post creation and updating.

## Screenshots:
<div>
<img src="https://github.com/redaxxxx/PostApp/blob/master/image1.jpeg" width="150">
<img src="https://github.com/redaxxxx/PostApp/blob/master/image2.jpeg" width="150">
<img src="https://github.com/redaxxxx/PostApp/blob/master/image3.jpeg" width="150">
</div>
