pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement { repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google();
        mavenCentral();
        maven( url= "https://jitpack.io" ) //zooming photoView
    }}

rootProject.name = "Resume"
include(":app")

rootProject.name = "Resume"
include(":app")
