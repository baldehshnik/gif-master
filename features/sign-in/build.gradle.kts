import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vd.study.sign_in"
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
        viewBinding = true
    }
}

dependencies {

    implementation(project(Build.Modules.Core))

    implementation(Build.Libs.Android.CoreKtx)
    implementation(Build.Libs.Android.AppCompat)
    implementation(Build.Libs.Android.FragmentKtx)
    implementation(Build.Libs.Android.NavigationFragmentKtx)
    implementation(Build.Libs.Android.NavigationUIKtx)

    implementation(Build.Libs.Image.Glide)

    implementation(Build.Libs.Google.Material)
    implementation(Build.Libs.Google.Hilt)
    kapt(Build.Libs.Google.HiltCompiler)

    testImplementation(Build.Libs.Testing.JUnit)
    androidTestImplementation(Build.Libs.Testing.JUnitEXT)
    androidTestImplementation(Build.Libs.Testing.EspressoCore)
}