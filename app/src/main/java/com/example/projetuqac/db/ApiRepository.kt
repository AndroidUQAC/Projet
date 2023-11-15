package com.example.projetuqac.db

import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    fun getPosts(): Flow<Result<List<Posts>>>
}