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

        object Testing {
            const val JUnit = "junit:junit:4.13.2"
            const val JUnitEXT = "androidx.test.ext:junit:1.1.5"
            const val EspressoCore = "androidx.test.espresso:espresso-core:3.5.1"
        }
    }
}