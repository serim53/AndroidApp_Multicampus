package com.example.blogapp.data.remote

import com.example.blogapp.model.Page
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {
    @GET("/v2/everything")
    suspend fun getList(
        @Query("q") q: String?,
        @Query("apiKey") apiKey: String?,
        @Query("page") page: Long,
        @Query("pageSize") pageSize: Int
    ): Page
}