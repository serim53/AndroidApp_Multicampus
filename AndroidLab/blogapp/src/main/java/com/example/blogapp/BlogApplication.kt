package com.example.blogapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//앱에서 하나만 작성 가능..
//manifest 에 등록해야 한다..
//앱 프로세스가 구동되자마자 최초 가장 먼저 생성, singleton 으로 유지..
//앱이 실행되면서 최초 한번 초기화 코드..

@HiltAndroidApp
class BlogApplication: Application() {
}