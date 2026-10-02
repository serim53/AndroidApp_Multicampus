package com.example.blogapp.ui.blog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.blogapp.domain.model.BlogPost
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(): ViewModel() {
    //post 데이터를 이곳에서 유지한다..
    //compose 다.. 이 데이터에 의해 composeable 이 갱신되어야 한다.. 상태
    //composable 의 상태를 viewmodel에 선언..
    //이 상태를 이용하는 composable 이 상태 변경시에 갱신된다..
    //개별 composable 에서 유지되는 상태가 아니다.. remember 필요 없다..
//    var posts by mutableStateOf(mutableListOf<BlogPost>())
//        private set//이 값 변경 가능하다(var), 단 외부에서는 안된다. 외부에서는 이용만 하라..

    private val _posts = MutableStateFlow(mutableListOf<BlogPost>())
    val posts: StateFlow<List<BlogPost>> = _posts.asStateFlow()

    //여러 포스트 중에서 현재 선택된.. edit, detail
//    var selectedPost: BlogPost? by mutableStateOf(null)
    private val _selectedPost = MutableStateFlow<BlogPost?>(null)
    val selectedPost: StateFlow<BlogPost?> = _selectedPost.asStateFlow()

    //개발자 임의 함수.. composable 에서 b/l 이 필요할때 호출..
    fun loadPost(id: Long){
        _selectedPost.value = _posts.value.find { it.id == id }
    }

    fun savePost(id: Long, title: String, content: String, imageUri: String?){
        //매개변수의 id 에 해당되는 데이터의 첫번째 index
        val index = _posts.value.indexOfFirst { it.id == id }
        if(index != -1){//이전에 저장된 것이 있다.. 수정..
            val existing = _posts.value[index]
            _posts.value[index] = existing.copy(
                title = title,
                content =  content,
                imageUri = imageUri
            )
        }else {
            //신규 저장..
            _posts.value.add(
                BlogPost(
                    id = _posts.value.size.toLong() + 1,
                    title = title,
                    content = content,
                    author = "author",
                    imageUri = imageUri
                )
            )
        }
    }
    fun deletePost(id: Long){
        _posts.value.removeAll { it.id == id }
    }
}