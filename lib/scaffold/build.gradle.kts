plugins {
    kotlin("android")
    kotlin("plugin.compose")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.scaffold"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":compose"))

    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.activity:activity-compose:1.12.1")
}
