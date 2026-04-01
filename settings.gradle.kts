@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
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

// Only the core feature remains enabled
includeProject(":feature-app", "features/app")

// Enabled integrations
includeProject(":integration-clearurl", "integration/clearurl")
includeProject(":integration-embed-resolve", "integration/embed-resolve")
includeProject(":integration-amp2html", "integration/amp2html")

fun includeProject(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}
