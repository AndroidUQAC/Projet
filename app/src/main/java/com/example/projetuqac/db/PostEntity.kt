package com.example.projetuqac.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_entity")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
