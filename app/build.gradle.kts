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
    compileSdk = 36

    defaultConfig {
        applicationId = "fe.linksheet"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-refactored"

        buildConfigField("String", "FLAVOR", "\"Full\"")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        aidl = true
    }
}

dependencies {
    implementation(project(":feature-app"))
    implementation(project(":feature-browser"))
    implementation(project(":feature-devicecompat"))
    implementation(project(":feature-downloader"))
    implementation(project(":feature-engine"))
    implementation(project(":feature-libredirect"))
    implementation(project(":feature-profile"))
    implementation(project(":feature-shizuku"))
    implementation(project(":feature-wiki"))
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
    implementation(project(":log"))

    implementation("androidx.compose.foundation:foundation:1.11.0-alpha01")
    implementation("androidx.compose.ui:ui:1.11.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.11.0-alpha01")
    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.google.android.material:material:1.13.0-alpha13")
    implementation("androidx.activity:activity-compose:1.12.1")
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("io.insert-koin:koin-android:4.2.0-beta2")
    implementation("io.insert-koin:koin-androidx-compose:4.2.0-beta2")
    implementation("io.insert-koin:koin-androidx-workmanager:4.2.0-beta2")
    implementation("com.github.1fexd.composekit:lifecycle-core:0.0.86")
    implementation("com.github.1fexd.composekit:koin:0.0.86")
    implementation("com.gitlab.grrfe.gson-ext:core:17.1.0-gson2-koin4")
    implementation("com.gitlab.grrfe.kotlin-ext:time-java:0.0.148")
    implementation("dev.rikka.tools.refine:runtime:4.0.0")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
}
