# AnNotes

This is a demo showcase that will apply clean architecture Note Android application.  

Development time: ~14 hours

In the main I had implemented:

* User Interface built with **[Jetpack Compose](https://developer.android.com/jetpack/compose)**
* A single-activity architecture,
  using **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)**.
* A presentation layer that contains a Compose screen (View) and a **ViewModel** per screen (or
  feature).
* Reactive UIs using **[Flow](https://developer.android.com/kotlin/flow)**.
* A **data layer** with a repository that fetch data from Firebase.
* Dependency injection
  using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

## DEMO

You can download this apk for app
demo: [Link](https://drive.google.com/file/d/1at8FKxovbMiHDjxaI_bZCLu47Ym7-HMM/view?usp=share_link)

## Setup

Clone the
repository: [https://github.com/anhoang241998/AnNotes.git](https://github.com/anhoang241998/AnNotes.git)

Open this demo in Android Studio.

Download `google-services.json` that was created in firebase console to attach to the `app` folder

> Note: If the project can't be build, please changed the JDK of the project and Android Studio to
> JDK 11

## Limitation  

- This app doesn't have the `domain` layer for not making it more complex.  
- This app doesn't cache the data that fetched from internet to local database (SQL).

