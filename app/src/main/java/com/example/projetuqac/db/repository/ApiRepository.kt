package com.example.projetuqac.db.repository

import com.example.projetuqac.db.api.Posts
import com.example.projetuqac.db.Result
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    fun getPosts(): Flow<Result<List<Posts>>>
}