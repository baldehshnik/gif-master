object Build {

    const val CompileSdk = 33
    const val MinSdk = 24

    object Libs {

        object Android {
            const val CoreKtx = "androidx.core:core-ktx:1.9.0"
            const val AppCompat = "androidx.appcompat:appcompat:1.6.1"
        }

        object Google {
            const val Material = "com.google.android.material:material:1.9.0"
        }

        object Network {
            const val Retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
            const val OkHttp = "com.squareup.okhttp3:okhttp:4.10.0"
            const val ConverterGson = "com.squareup.retrofit2:converter-gson:2.3.0"
        }

        object Testing {
            const val JUnit = "junit:junit:4.13.2"
            const val JUnitEXT = "androidx.test.ext:junit:1.1.5"
            const val EspressoCore = "androidx.test.espresso:espresso-core:3.5.1"
        }
    }
}