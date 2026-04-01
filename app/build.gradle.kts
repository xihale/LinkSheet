plugins {
    kotlin("android")
    kotlin("plugin.compose")
    kotlin("plugin.serialization")
    id("com.android.application")
    id("kotlin-parcelize")
    id("dev.rikka.tools.refine")
}

android {
    namespace = "fe.linksheet"
    compileSdk = 34

    defaultConfig {
        applicationId = "fe.linksheet"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0-refactored"

        buildConfigField("String", "FLAVOR", "\"Full\"")
        buildConfigField("String", "APTABASE_API_KEY", "\"\"")
        buildConfigField("Boolean", "ANALYTICS_SUPPORTED", "true")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation(project(":feature-app"))
    implementation(project(":core-ui"))
    implementation(project(":integration-clearurl"))
    implementation(project(":integration-embed-resolve"))
    implementation(project(":integration-amp2html"))

    implementation(project(":util"))
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":compose"))
    implementation(project(":scaffold"))
    implementation(project(":hidden-api"))

    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("dev.rikka.tools.refine:runtime:4.0.0")
}
