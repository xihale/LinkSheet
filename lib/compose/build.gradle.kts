plugins {
    kotlin("android")
    kotlin("plugin.compose")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.compose"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":util"))
    implementation(project(":api"))
    implementation(project(":core-ui"))

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation("androidx.compose.runtime:runtime:1.11.0-alpha01")
    implementation("androidx.compose.foundation:foundation:1.11.0-alpha01")
    implementation("androidx.compose.ui:ui:1.11.0-alpha01")
    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
}
