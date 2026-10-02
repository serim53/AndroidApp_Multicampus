package com.example.blogapp.model

data class News(
    val id: Long = 0,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null
)

data class Page(
    val articles: List<News>? = null
)