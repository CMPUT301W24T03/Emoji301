// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
buildscript {
//    extra["kotlin_version"] = "1.4.10"
//    val kotlin_version = "1.4.10"
    repositories {
        google()
    }
    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}