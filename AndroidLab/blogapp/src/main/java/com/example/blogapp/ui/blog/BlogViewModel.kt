package com.example.blogapp.ui.blog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.preferences.UserPreferences
import com.example.blogapp.data.repository.BlogRepository
import com.example.blogapp.domain.model.BlogPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repository: BlogRepository,
    private val userPreferences: UserPreferences
): ViewModel() {


    val email: StateFlow<String> = userPreferences.email.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val posts: StateFlow<List<BlogPost>> = repository.allPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedPost = MutableStateFlow<BlogPost?>(null)
    val selectedPost: StateFlow<BlogPost?> = _selectedPost.asStateFlow()

    fun loadPost(id: Long) {
        Log.d("BlogViewModel", "loadPost..........")

        viewModelScope.launch {
            val post = repository.getPostById(id)
            _selectedPost.value = post
        }
    }

    fun savePost(id: Long, title: String, content: String, imageUri: String?) {
        Log.d("BlogViewModel", "savePost..........$id")

        viewModelScope.launch {
            if (title.isBlank()) return@launch
            val author = userPreferences.email.first().ifBlank { "Unknown" }
            if (id > 0) {
                repository.updatePost(

                    BlogPost(
                        id = id,
                        title = title,
                        content = content,
                        author = author,
                        imageUri = imageUri,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } else {
                repository.insertPost(
                    BlogPost(
                        title = title,
                        content = content,
                        author = author,
                        imageUri = imageUri
                    )
                )
            }
        }
    }

    fun deletePost(id: Long) {
        Log.d("BlogViewModel", "deletePost..........")
        viewModelScope.launch {
            repository.deletePost(id)
        }
    }
}