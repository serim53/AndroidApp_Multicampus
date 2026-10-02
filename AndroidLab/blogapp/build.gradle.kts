plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.blogapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.blogapp"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //platform api 이외에 라이브러리 사용 등록, 다운로드..
    //Sync Now 순간 다운로드..
    //implementation("group_id:artifact_id:version")
    //하나의 프로젝트 내에 여러 모듈이 있고, 모듈별로 라이브러리 등록하는데, 동일 라이브러리를 중복 등록하는 경우가 있다..
    //라이브러리 버전 변경시에 모든 모듈의 build.gradle.kts 를 수정하는 것이 불편하다고 봐서...
    //라이브러리 등록을 libs.versions.toml 에 통합등록하고.. 개별 모듈에서는 링크로 연결해서 사용..
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    //이미지 획득 및 이미지 핸들링 전문 라이브러리..
    //안드로이드에서 이미지 핸들링시 이용하는 유명한 라이브러리가.. Glide, Coil(coroutine 기본 지원 - KOTLIN 개발하는 경우 선호..)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.9.8")

    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("com.google.code.gson:gson:2.10.1")
    // DataStore (Key - Value)
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
}





