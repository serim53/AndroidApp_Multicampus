package com.example.blogapp.domain.model


//data 로 선언, 주 생성자의 매개변수 선언
//==>멤버 변수 선언. 생성자 매개변수로 멤버변수 초기화, setter, getter 함수 추가
//toString 함수 오버라이드, equals 함수 오버라이드..
data class BlogPost(
    val id: Long = 0,
    val title: String,
    val content: String,
    val author: String,
    val imageUri: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis()
)