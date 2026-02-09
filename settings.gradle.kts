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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MusicApplication"
include(":app")
include(":infrastructure")
include(":presentation-common")
include(":feature-home")
include(":feature-player")
include(":feature-library")
include(":feature-search")
include(":feature-discovery")
include(":feature-song")
include(":feature-playlist")
include(":feature-favorite")
include(":feature-user")
include(":feature-artist")
include(":feature-album")
include(":feature-recent")
include(":feature-recommended")
include(":feature-foryou")
include(":feature-mostheard")
include(":core-model")
include(":core-utils")
include(":core-domain")
include(":core-resources")
include(":core-ui")
include(":core-navigation")
include(":core-playback")
include(":core-database")
include(":core-network")
