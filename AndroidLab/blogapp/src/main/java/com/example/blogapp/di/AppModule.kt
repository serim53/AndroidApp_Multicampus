package com.example.blogapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//라이브러리 클래스를 hilt 로 이용하고 싶은데, 우리가 작성한 클래스가 아니니.. 생성자를 건드릴 수 없어서..
//타입을 정확하게 알려주기 위해서..
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //라이브러리 클래스를 직접 준비해서. hilt 에 알려주겠다..
}