plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.common"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    api(project(":api"))
    implementation("androidx.core:core-ktx:1.17.0")
}
