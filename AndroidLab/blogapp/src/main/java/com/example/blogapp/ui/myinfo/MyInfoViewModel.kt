package com.example.blogapp.ui.myinfo

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//AndroidViewModel 상속받으면 자동으로 Application 객체 전달..
//class MyInfoViewModel(application: Application): AndroidViewModel(application) {

@HiltViewModel
class MyInfoViewModel @Inject constructor(): ViewModel(){
    //컴포즈의 상태로 데이터를 공유하는 방법..
//    var email by mutableStateOf("")
//        private set

    //코루틴 flow 활용..
    //MutableStateFlow : 변경가능
    //StateFlow : 구독만 가능..
    private val _email = MutableStateFlow("")
    //외부에서 이용.. 외부에서 데이터 구독만 가능하고 직접 변경 못하게 하려고..
    val email: StateFlow<String> = _email.asStateFlow()

    fun saveEmail(email: String){
        _email.value = email.trim()
    }
}