package com.example.projetuqac.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projetuqac.db.api.ApiDao
import com.example.projetuqac.db.models.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postDao(): ApiDao
}