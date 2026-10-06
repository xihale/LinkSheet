@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    resolutionStrategy {
        eachPlugin {
            val version = requested.version ?: "0.0.79"
            when (requested.id.id) {
                "com.gitlab.grrfe.build-settings-plugin" -> useModule("com.gitlab.grrfe.gradle-build:build-settings:$version")
                "com.gitlab.grrfe.new-build-logic-plugin" -> useModule("com.gitlab.grrfe.gradle-build:new-build-logic:$version")
            }
        }
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.6"
        id("com.gitlab.grrfe.build-settings-plugin") version "0.0.79"
        id("com.gitlab.grrfe.new-build-logic-plugin") version "0.0.79"
    }
}

plugins {
    id("de.fayard.refreshVersions")
    id("com.gitlab.grrfe.build-settings-plugin")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "LinkSheet"

include(":app", ":config")
includeProject(":core-ui", "lib/core-ui")
includeProject(":scaffold", "lib/scaffold")
includeProject(":hidden-api", "lib/hidden-api")
includeProject(":util", "lib/util")
includeProject(":api", "lib/api")
includeProject(":log", "lib/log")
includeProject(":common", "lib/common")
includeProject(":compose", "lib/compose")

includeProject(":feature-app", "features/app")
includeProject(":feature-browser", "features/browser")
includeProject(":feature-devicecompat", "features/devicecompat")
includeProject(":feature-downloader", "features/downloader")
includeProject(":feature-engine", "features/engine")
includeProject(":feature-libredirect", "features/libredirect")
includeProject(":feature-profile", "features/profile")
includeProject(":feature-shizuku", "features/shizuku")
includeProject(":feature-systeminfo", "features/systeminfo")
includeProject(":feature-wiki", "features/wiki")

includeProject(":sdk-common", "sdk/common")
includeProject(":sdk-rule-plugin", "sdk/rule-plugin")

includeProject(":integration-clearurl", "integration/clearurl")
includeProject(":integration-embed-resolve", "integration/embed-resolve")
includeProject(":integration-amp2html", "integration/amp2html")

includeProject(":test-core", "test-lib/core")
includeProject(":test-fake", "test-lib/fake")
includeProject(":test-instrument", "test-lib/instrument")
includeProject(":test-koin", "test-lib/koin")

fun includeProject(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}


