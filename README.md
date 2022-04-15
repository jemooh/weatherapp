# Weather-app

Requirements
----

- Create a weather application tp display the current weather at the user's location and a 5-day
  forecast.

### How it's built

* Technologies used
    * [Kotlin](https://kotlinlang.org/)
    * [Compose](https://developer.android.com/jetpack/compose)
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    * [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
    * [KOIN Dependency](https://insert-koin.io/)
    * [Retrofit](https://square.github.io/retrofit/)
    * [Jetpack](https://developer.android.com/jetpack)
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Timber](https://github.com/JakeWharton/timber)
    * [Leak Canary](https://github.com/square/leakcanary)

* Architecture
  * MVVM - Model View View Model

* Tests
  * [JUnit5](https://junit.org/junit5/)
  * [MockK](https://github.com/mockk/mockk)

* CI/CD
  * Github Actions
  * [Fastlane](https://fastlane.tools)

### Weather app screenshots

| Current Weather by User Location | | --------------- | ---------------- |
| <img src="current_weather.jpg" height="480"> |

| Saved Favourite weather list | | --------------- | ---------------- |
|  <img src="weather_favourite_list.jpg" height="480"> |

| Saved Favourite weather on Google map | | --------------- | ---------------- |
| <img src="weather_favourite_map.jpg" height="480"> | 