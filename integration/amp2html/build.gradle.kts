plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "fe.linksheet.integration.amp2html"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    api(project(":api"))
}
