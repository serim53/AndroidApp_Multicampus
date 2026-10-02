package com.example.blogapp.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.repository.NewsRepository
import com.example.blogapp.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val API_KEY = "079dac74a5f94ebdb990ecf61c8854b7"

sealed interface NewsUiState {
    data object Loading : NewsUiState
    data class Success(val newsList: List<News>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews(query: String = "swiss") {
        viewModelScope.launch {

            _uiState.value = NewsUiState.Loading
            try {
                val newsList = repository.getNewsList(
                    query = query,
                    apiKey = API_KEY,
                    page = 1,
                    pageSize = 10
                )
                _uiState.value = NewsUiState.Success(newsList)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}