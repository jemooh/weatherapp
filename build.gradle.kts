// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.0-RC2"
    id("com.diffplug.spotless") version "5.9.0"
    id("com.android.library") version "7.1.2" apply false
    id("com.android.application") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.dokka") version "1.4.20"
    id("com.github.ben-manes.versions") version "0.29.0"
}

allprojects {

    apply(plugin = "org.jetbrains.dokka")

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        android.set(true)
        verbose.set(true)
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}

buildscript {
    val jacocoVersion by extra("0.2")
    val junit5Version by extra("1.7.1.1")

    dependencies {
        classpath("com.hiya:jacoco-android:$jacocoVersion")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:$junit5Version")
    }
}
