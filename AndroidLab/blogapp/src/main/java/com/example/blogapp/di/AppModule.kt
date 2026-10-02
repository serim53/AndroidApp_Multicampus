package com.example.blogapp.di

import android.content.Context
import androidx.room.Room
import com.example.blogapp.data.local.BlogDatabase
import com.example.blogapp.data.local.dao.BlogPostDao
import com.example.blogapp.data.remote.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BlogDatabase =
        Room.databaseBuilder(
            context,
            BlogDatabase::class.java,
            "blog_database"
        ).build()

    @Provides
    @Singleton
    fun provideBlogPostDao(db: BlogDatabase): BlogPostDao = db.blogPostDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)
}