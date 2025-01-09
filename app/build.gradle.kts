import androidx.room.gradle.RoomExtension

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.daggerDiLibrarry)
    alias(libs.plugins.room)
    alias(libs.plugins.androidKsp)
    id("kotlin-kapt")
    alias(libs.plugins.jetpackNavigationSafeArgs)
}

android {
    signingConfigs {
        create("release") {
        }
    }
    namespace = "com.rrpvm.pstu_curs_rrpvm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rrpvm.pstu_curs_rrpvm"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildToolsVersion = "34.0.0"
}
extensions.configure<RoomExtension> {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)



    //navigation
    implementation(libs.jetpack.navigation.ui)
    implementation(libs.jetpack.navigation.fragment)


    //DI HILT
    implementation(libs.dagger.lib)
    ksp(libs.dagger.compiler)
    //END DI HILT
    //ROOM
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    //END ROOM

    implementation(libs.splash.screen)
    implementation(libs.lottie.anims)

    //shared
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
    //features
    implementation(project(":feature:authorization"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:kinofeed"))






    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}