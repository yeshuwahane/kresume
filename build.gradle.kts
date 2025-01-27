// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

}

allprojects {
    configurations {
        all {
            resolutionStrategy {
                dependencySubstitution {
                    substitute(module("com.github.barteksc:android-pdf-viewer"))
                        .using(module("com.github.DImuthuUpe:AndroidPdfViewer:3.2.0-beta.1"))
                        .because("Library is in a process of ownership transfer. Jitpack and Jcenter are not reliable.")
                }
            }
        }
    }
}