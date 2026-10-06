plugins {
    kotlin("android")
    kotlin("plugin.compose")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "app.linksheet.feature.app"
    compileSdk = 36
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

    implementation(platform("com.github.1fexd.composekit:platform:0.0.86"))
    implementation("com.github.1fexd.composekit:compose-core")
    implementation("com.github.1fexd.composekit:compose-component")
    implementation("com.github.1fexd.composekit:core")
    implementation("com.github.1fexd.composekit:preference-compose-core")
    implementation("com.github.1fexd.composekit:preference-compose-core2")

    implementation("androidx.compose.ui:ui:1.11.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.11.0-alpha01")
    implementation("androidx.compose.foundation:foundation:1.11.0-alpha01")
    implementation("androidx.compose.material3:material3:1.5.0-alpha10")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("io.github.reandroid:ARSCLib:1.3.8")
    implementation("com.gitlab.grrfe.kotlin-ext:result-core:0.0.148")
}
