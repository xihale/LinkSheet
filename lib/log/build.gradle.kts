plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.log"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("org.mozilla.components:support-utils:145.0b9")
}
