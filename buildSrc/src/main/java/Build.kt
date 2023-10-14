object Build {

    const val CompileSdk = 33
    const val MinSdk = 24

    object Libs {

        object Android {
            const val RoomVersion = "2.5.2"

            const val CoreKtx = "androidx.core:core-ktx:1.9.0"
            const val AppCompat = "androidx.appcompat:appcompat:1.6.1"

            const val Room = "androidx.room:room-runtime:$RoomVersion"
            const val RoomKtx = "androidx.room:room-ktx:$RoomVersion"
            const val RoomCompiler = "androidx.room:room-compiler:$RoomVersion"

            const val FragmentKtx = "androidx.fragment:fragment-ktx:1.6.1"

            const val Paging = "androidx.paging:paging-runtime:3.2.1"
        }

        object Google {
            const val HiltVersion = "2.44"

            const val Material = "com.google.android.material:material:1.9.0"
            const val Hilt = "com.google.dagger:hilt-android:$HiltVersion"
            const val HiltCompiler = "com.google.dagger:hilt-android-compiler:$HiltVersion"
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
            const val CoroutinesTesting = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9"
            const val HiltTesting = "com.google.dagger:hilt-android-testing:${Google.HiltVersion}"
        }
    }

    object Modules {
        const val Core = ":core"
        const val Data = ":data"

        object Features {
            const val Account = ":features:account"
            const val SingUp = ":features:sign-up"
            const val SingIn = ":features:sign-in"
            const val Home = ":features:home"
            const val Settings = ":features:settings"
        }
    }
}