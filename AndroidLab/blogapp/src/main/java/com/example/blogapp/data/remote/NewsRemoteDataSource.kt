package com.example.blogapp.data.remote

import com.example.blogapp.model.Page
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val apiService: NewsApiService
) {
    suspend fun getNewsList(query: String, apiKey: String, page: Long, pageSize: Int): Page =
        apiService.getList(query, apiKey, page, pageSize)
}