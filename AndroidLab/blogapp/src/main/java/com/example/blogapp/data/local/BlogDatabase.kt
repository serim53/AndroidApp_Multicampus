package com.example.blogapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blogapp.data.local.dao.BlogPostDao
import com.example.blogapp.data.local.entity.BlogPostEntity

@Database(
    entities = [BlogPostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BlogDatabase : RoomDatabase() {
    abstract fun blogPostDao(): BlogPostDao
}