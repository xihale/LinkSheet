plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "fe.linksheet.integration.embedresolve"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    api(project(":api"))
    api(platform("com.gitlab.grrfe.gson-ext:platform:17.1.0-gson2-koin4"))
    implementation("com.gitlab.grrfe.gson-ext:core")
    implementation("com.google.code.gson:gson:2.13.2")
}
