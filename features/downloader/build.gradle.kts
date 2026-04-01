import com.gitlab.grrfe.gradlebuild.android.AndroidSdk
import fe.buildlogic.Version
import fe.buildlogic.common.OptIn
import fe.buildlogic.common.extension.addOptIn

plugins {
    kotlin("android")
    kotlin("plugin.serialization")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.feature.downloader"
    compileSdk = AndroidSdk.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidSdk.MIN_SDK
    }

    kotlin {
        jvmToolchain(Version.JVM)
        addOptIn(OptIn.ExperimentalTime)
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":util"))

    implementation(Koin.android)
    implementation(Koin.compose)
    implementation(AndroidX.core.ktx)
    
    implementation(platform("androidx.compose:compose-bom-alpha:_"))
    implementation(AndroidX.compose.runtime)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.material3)

    implementation(JetBrains.ktor.client.core)
    implementation(JetBrains.ktor.client.gson)
    implementation(JetBrains.ktor.client.okHttp)
    implementation(JetBrains.ktor.client.android)
    implementation(JetBrains.ktor.client.logging)
    implementation(JetBrains.ktor.client.contentNegotiation)
    implementation(JetBrains.ktor.client.json)
    implementation(JetBrains.ktor.client.encoding)
    implementation(JetBrains.ktor.plugins.serialization.gson)
    testImplementation(JetBrains.ktor.client.mock)

    testImplementation(Testing.robolectric)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(AndroidX.test.ext.junit.ktx)
    testImplementation(project(":test-core"))
}
