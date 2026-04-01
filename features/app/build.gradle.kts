plugins {
    kotlin("android")
    kotlin("plugin.compose")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "app.linksheet.feature.app"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":compose"))
    implementation(project(":util"))
    
    implementation("androidx.compose.ui:ui:1.11.0-alpha01")
    implementation("androidx.compose.foundation:foundation:1.11.0-alpha01")
    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("io.github.reandroid:ARSCLib:1.3.8")
}
