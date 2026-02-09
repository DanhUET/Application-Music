import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.devtools)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "pro.branium.infrastructure"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-utils"))
    implementation(project(":core-domain"))
    implementation(project(":core-ui"))
    implementation(project(":core-network"))
    implementation(project(":core-navigation"))
    implementation(project(":core-database"))
    implementation(project(":core-playback"))

    implementation(project(":feature-album"))
    implementation(project(":feature-artist"))
    implementation(project(":feature-song"))
    implementation(project(":feature-player"))
    implementation(project(":feature-playlist"))
    implementation(project(":feature-favorite"))
    implementation(project(":feature-user"))
    implementation(project(":feature-recent"))
    implementation(project(":feature-search"))
    implementation(project(":feature-recommended"))
    implementation(project(":feature-foryou"))
    implementation(project(":feature-mostheard"))

    // remote api with gson and glide
    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.gson.converter)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.paging.runtime)
    implementation(libs.paging.runtime.ktx)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.media3.xoplayer)
    implementation(libs.media3.common)
    implementation(libs.media3.media.session)
    implementation(libs.media3.ui)

    // for viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}