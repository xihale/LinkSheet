plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.util"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    api(project(":api"))
    api(project(":common"))
    implementation("androidx.core:core-ktx:1.12.0") // Standard stable version
    implementation("io.ktor:ktor-client-core:2.3.7") // Standard stable version
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // Standard stable version
    implementation("org.jsoup:jsoup:1.17.2")
}
