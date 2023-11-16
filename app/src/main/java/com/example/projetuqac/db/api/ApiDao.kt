package com.example.projetuqac.db.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projetuqac.db.models.PostEntity

@Dao
interface ApiDao {

    @Query("SELECT * FROM posts_entity")
    suspend fun getPosts(): List<PostEntity>

    @Query("SELECT * FROM posts_entity WHERE id = :id")
    suspend fun getPost(id: Int): PostEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)
}