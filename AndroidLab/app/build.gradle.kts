plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.androidlab"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.androidlab"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    //ViewBinding 사용하겠다고 선언.. 선언만으로.. layout xml 당 XXXBinding 클래스가 자동으로 만들어진다.
    //build.gradle.kts 수정하면 수정사항 반영하라고 꼭 Sync Now 클릭..
    viewBinding.enable = true
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}