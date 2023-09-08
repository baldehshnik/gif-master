import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vd.study.data"
    compileSdk = Build.CompileSdk

    defaultConfig {
        minSdk = Build.MinSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    tasks.withType<KaptGenerateStubsTask> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(Build.Libs.Android.CoreKtx)
    implementation(Build.Libs.Android.AppCompat)

    implementation(Build.Libs.Google.Material)
    implementation(Build.Libs.Google.Hilt)
    kapt(Build.Libs.Google.Compiler)

    implementation(Build.Libs.Network.Retrofit2)
    implementation(Build.Libs.Network.OkHttp)
    implementation(Build.Libs.Network.ConverterGson)

    implementation(Build.Libs.Testing.JUnit)
    androidTestImplementation(Build.Libs.Testing.JUnitEXT)
    androidTestImplementation(Build.Libs.Testing.EspressoCore)
}