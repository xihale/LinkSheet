plugins {
    kotlin("android") version "2.2.20" apply false
    kotlin("plugin.compose") version "2.2.20" apply false
    kotlin("plugin.serialization") version "2.2.20" apply false
    id("com.android.application") version "8.10.0" apply false
    id("com.android.library") version "8.10.0" apply false
    id("dev.rikka.tools.refine") version "4.0.0" apply false
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
