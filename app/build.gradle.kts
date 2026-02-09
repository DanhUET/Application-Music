import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.safeargs.kotlin)
    alias(libs.plugins.google.devtools)
    alias(libs.plugins.dagger.hilt.android)
//    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "pro.branium.musicapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "pro.branium.musicapp"
        minSdk = 28
        targetSdk = 36
        versionCode = 18
        versionName = "v2025.1.18"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget("21")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // khai báo module cần sử dụng
    implementation(project(":presentation-common"))
    implementation(project(":feature-home"))
    implementation(project(":feature-song"))
    implementation(project(":feature-album"))
    implementation(project(":feature-library"))
    implementation(project(":feature-playlist"))
    implementation(project(":feature-discovery"))
    implementation(project(":feature-artist"))
    implementation(project(":feature-user"))
    implementation(project(":feature-player"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-utils"))
    implementation(project(":core-navigation"))
    implementation(project(":core-playback"))
    implementation(project(":infrastructure"))

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.preference)
    // for navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.runtime)

    // for viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity)

    // dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // for viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // hilt navigation
    implementation(libs.androidx.hilt.navigation.fragment)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}