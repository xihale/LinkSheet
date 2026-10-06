import com.gitlab.grrfe.gradlebuild.android.AndroidSdk
import fe.build.dependencies.Grrfe
import fe.build.dependencies._1fexd
import fe.buildlogic.Version

plugins {
    kotlin("android")
    kotlin("plugin.serialization")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.feature.devicecompat"
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
    implementation(project(":common"))
    implementation(project(":util"))
    implementation(project(":feature-systeminfo"))

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("io.insert-koin:koin-android:4.1.1")

    testImplementation(AndroidX.test.ext.junit.ktx)
    testImplementation(project(":test-core"))
    testImplementation(Grrfe.std.test)
    testImplementation("com.willowtreeapps.assertk:assertk:_")
}
