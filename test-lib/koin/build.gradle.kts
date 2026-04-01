import com.gitlab.grrfe.gradlebuild.android.AndroidSdk
import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
}

group = "app.linksheet.testlib.koin"

android {
    namespace = group.toString()
    compileSdk = AndroidSdk.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidSdk.MIN_SDK
    }

    kotlin {
        jvmToolchain(Version.JVM)
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(Koin.core)
    api(Koin.test)
}
