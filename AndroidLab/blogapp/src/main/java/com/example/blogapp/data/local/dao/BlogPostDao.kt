package com.example.blogapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.blogapp.data.local.entity.BlogPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogPostDao {

    @Query("SELECT * FROM blog_posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<BlogPostEntity>>

    @Query("SELECT * FROM blog_posts WHERE id = :id")
    suspend fun getPostById(id: Long): BlogPostEntity?

    @Insert
    suspend fun insertPost(post: BlogPostEntity): Long

    @Update
    suspend fun updatePost(post: BlogPostEntity)

    @Query("DELETE FROM blog_posts WHERE id = :id")
    suspend fun deletePostById(id: Long)
}