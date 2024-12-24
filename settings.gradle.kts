pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "pstu_curs_rrpvm"
include(":app")
include(":domain")
include(":core")
include(":feature:profile")
include(":feature:authorization")
include(":data")
