plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "app.linksheet.api"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("io.insert-koin:koin-core:4.2.0-beta2")
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.core:core-ktx:1.17.0")

    api(platform("com.github.1fexd.composekit:platform:0.0.86"))
    api("com.github.1fexd.composekit:preference-core")
    api("com.github.1fexd.composekit:preference-compose-core2")

    api(platform("com.gitlab.grrfe.kotlin-ext:platform:0.0.148"))
    api("com.gitlab.grrfe.kotlin-ext:result-core")
    api("com.gitlab.grrfe.kotlin-ext:uri")
}
