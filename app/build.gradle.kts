plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.hiya.jacoco-android")
    id("de.mannodermaus.android-junit5")
    id("kotlin-parcelize")
}

jacoco {
    toolVersion = "0.8.4"
}

android {
    compileSdk = 32/**/

    defaultConfig {
        applicationId = "com.jkirwa.weatherapp"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        animationsDisabled = true
        junitPlatform.apply {
            filters {
                includeEngines("spek2")
            }
            jacocoOptions {
                html.enabled = true
                xml.enabled = false
                csv.enabled = false
            }
        }
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            all {
                testCoverage {
                }
            }
        }
    }

    // Fixes - No tests found for given includes Error, when running Parameterized Unit test in Android Studio
    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            lifecycle {
                events = mutableSetOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
                )
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                showStandardStreams = true
            }
            info.events = lifecycle.events
            info.exceptionFormat = lifecycle.exceptionFormat
        }

        val failedTests = mutableListOf<TestDescriptor>()
        val skippedTests = mutableListOf<TestDescriptor>()

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                when (result.resultType) {
                    TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                    TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                    else -> Unit
                }
            }

            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                if (suite.parent == null) { // root suite
                    logger.lifecycle("----")
                    logger.lifecycle("Test result: ${result.resultType}")
                    logger.lifecycle(
                        "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped"
                    )
                    failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                    skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
                }
            }

            private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
                logger.lifecycle(subject)
                forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
            }

            private fun TestDescriptor.displayName() =
                parent?.let { "${it.name} - $name" } ?: name
        })
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = " - debug"
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.compose.compiler:compiler:1.1.1")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0-alpha06")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-ktx:1.4.0")

    // Koin MVVM
    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("io.insert-koin:koin-android:3.1.2")
    implementation("io.insert-koin:koin-androidx-compose:3.1.2")
    // Koin testing tools
    testImplementation("io.insert-koin:koin-test:3.1.2")
    // Needed JUnit version
    testImplementation("io.insert-koin:koin-test-junit4:3.1.2")

    // Room
    implementation("androidx.room:room-runtime:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.4.2")

    implementation("com.jakewharton.timber:timber:5.0.0")

    // Location service
    implementation("com.google.android.gms:play-services-location:17.0.0")

    // Compose
    implementation("androidx.compose.runtime:runtime:1.1.1")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.foundation:foundation:1.1.1")
    implementation("androidx.compose.foundation:foundation-layout:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.compose.ui:ui-tooling:1.1.1")
    implementation("com.google.android.material:compose-theme-adapter:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")

    // navigation
    implementation("androidx.navigation:navigation-fragment:2.5.0-alpha04")
    implementation("androidx.navigation:navigation-ui:2.5.0-alpha04")
    implementation("androidx.navigation:navigation-compose:2.4.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.23.1")

    // Google maps
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    // implementation")com.google.maps.android:maps-v3-ktx:2.2.0")
    implementation("androidx.fragment:fragment:1.4.1")

    implementation("com.google.maps.android:maps-ktx:3.4.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")
    implementation("com.google.android.gms:play-services-maps:18.0.2")

    // Testing
    // leak canary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")

    // UI Test - Compose
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.0-alpha01")
    // Needed for createComposeRule, but not createAndroidComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.0-alpha01")

    // spek
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.18")
    testImplementation("org.spekframework.spek2:spek-runner-junit5:2.0.18")

    // spek requires kotlin-reflect, omit when already in classpath
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")

    testImplementation("io.insert-koin:koin-test-junit5:3.1.2")
    testImplementation("io.insert-koin:koin-test:3.1.2")

    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("io.mockk:mockk-agent-jvm:1.12.0") {
        because(
            "This dependency resolves the NoClassDefFoundError when using spek " +
                "https://github.com/mockk/mockk/issues/605," +
                "https://github.com/spekframework/spek/issues/968"
        )
    }

    // JUnit5 dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.6.0") {
        because(
            "This allows us to run JUnit4 tests as well as Spek tests (JUnit5) from the command line." +
                "Found in: https://github.com/spekframework/spek/issues/232#issuecomment-610732158"
        )
    }

    // Mock Web Server
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.2")

    // RoomDB Test
    testImplementation("androidx.room:room-testing:2.4.0-alpha04")

    testImplementation("androidx.test:core:1.4.1-alpha01")

    testImplementation("androidx.test:runner:1.4.0")

    // Truth assertion lib
    testImplementation("com.google.truth:truth:1.1.3")

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")

    // For InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // test kotlinx.coroutines Flow
    testImplementation("app.cash.turbine:turbine:0.6.0")

    // Roboelectric - Testing Room
    testImplementation("org.robolectric:robolectric:4.6.1")

    testImplementation("app.cash.turbine:turbine:0.7.0")
}
