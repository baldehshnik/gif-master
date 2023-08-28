plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Build.Libs.Android.CoreKtx)
    implementation(Build.Libs.Android.AppCompat)
    implementation(Build.Libs.Google.Material)
    testImplementation(Build.Libs.Testing.JUnit)
    androidTestImplementation(Build.Libs.Testing.JUnitEXT)
    androidTestImplementation(Build.Libs.Testing.EspressoCore)
}