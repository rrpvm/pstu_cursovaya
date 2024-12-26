// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    //android
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    //hilt
    alias(libs.plugins.daggerDiLibrarry) apply false
    //ksp
    alias(libs.plugins.androidKsp) apply false
    //kotlin
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //safe args jetpack navigation
    alias(libs.plugins.jetpackNavigationSafeArgs) apply false
    alias(libs.plugins.room) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

