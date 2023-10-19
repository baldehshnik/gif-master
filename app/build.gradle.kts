plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vd.study.gif_master"
    compileSdk = Build.CompileSdk

    defaultConfig {
        applicationId = "com.vd.study.gif_master"
        minSdk = Build.MinSdk
        targetSdk = 33
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(Build.Modules.Core))
    implementation(project(Build.Modules.Data))
    implementation(project(Build.Modules.Features.Account))
    implementation(project(Build.Modules.Features.SingUp))
    implementation(project(Build.Modules.Features.SingIn))
    implementation(project(Build.Modules.Features.Home))
    implementation(project(Build.Modules.Features.Settings))

    implementation(Build.Libs.Android.CoreKtx)
    implementation(Build.Libs.Android.AppCompat)
    implementation(Build.Libs.Android.Paging)
    implementation(Build.Libs.Android.NavigationFragmentKtx)
    implementation(Build.Libs.Android.NavigationUIKtx)

    implementation(Build.Libs.Google.Material)
    implementation(Build.Libs.Google.Hilt)
    kapt(Build.Libs.Google.HiltCompiler)

    testImplementation(Build.Libs.Testing.JUnit)
    androidTestImplementation(Build.Libs.Testing.JUnitEXT)
    androidTestImplementation(Build.Libs.Testing.EspressoCore)
}