plugins {
    kotlin("android")
    kotlin("plugin.compose")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.core.ui"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("androidx.compose.foundation:foundation:1.11.0-alpha01")
    implementation("androidx.compose.ui:ui:1.11.0-alpha01")
}
