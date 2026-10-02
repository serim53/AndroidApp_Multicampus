package com.example.blogapp.data.repository

import com.example.blogapp.data.local.dao.BlogPostDao
import com.example.blogapp.data.local.entity.BlogPostEntity
import com.example.blogapp.domain.model.BlogPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepository @Inject constructor(
    private val dao: BlogPostDao
) {

    private fun BlogPostEntity.toDomain() = BlogPost(
        id = id,
        title = title,
        content = content,
        author = author,
        imageUri = imageUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun BlogPost.toEntity() = BlogPostEntity(
        id = id,
        title = title,
        content = content,
        author = author,
        imageUri = imageUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )


    val allPosts: Flow<List<BlogPost>> = dao.getAllPosts().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun getPostById(id: Long): BlogPost? =
        dao.getPostById(id)?.toDomain()


    suspend fun insertPost(post: BlogPost): Long =
        dao.insertPost(post.toEntity())


    suspend fun updatePost(post: BlogPost) =
        dao.updatePost(post.toEntity())


    suspend fun deletePost(id: Long) =
        dao.deletePostById(id)
}