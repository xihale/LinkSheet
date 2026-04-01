plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.hiddenapi"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.9.1")
    implementation("dev.rikka.tools.refine:runtime:4.0.0")
}
