plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.common"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
    sourceSets {
        getByName("main") {
            kotlin.srcDir("src/main/compat")
        }
    }
}

dependencies {
    api(project(":api"))
    implementation("androidx.core:core-ktx:1.17.0")
}
