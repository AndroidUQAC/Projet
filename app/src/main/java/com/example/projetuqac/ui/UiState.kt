package com.example.projetuqac.ui

import com.example.projetuqac.db.api.Posts

data class UiState(
    val isLoading: Boolean = false,
    val posts: List<Posts>? = null,
    val error: String? = ""
)
