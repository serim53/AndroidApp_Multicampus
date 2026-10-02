package com.example.blogapp.data.repository

import com.example.blogapp.data.remote.NewsRemoteDataSource
import com.example.blogapp.model.News
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
) {
    suspend fun getNewsList(
        query: String,
        apiKey: String,
        page: Long = 1,
        pageSize: Int = 10
    ): List<News> {
        val result = remoteDataSource.getNewsList(query, apiKey, page, pageSize)
        return result.articles ?: emptyList()
    }
}