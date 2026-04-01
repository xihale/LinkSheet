import com.gitlab.grrfe.gradlebuild.android.AndroidSdk
import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "fe.linksheet.feature.systeminfo"
    compileSdk = AndroidSdk.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidSdk.MIN_SDK
    }

    kotlin {
        jvmToolchain(Version.JVM)
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":util"))
    compileOnly(project(":hidden-api"))

    implementation("com.google.code.gson:gson:_")
    implementation(AndroidX.core.ktx)

    testImplementation(AndroidX.test.ext.junit.ktx)
    testImplementation(project(":test-core"))
}
