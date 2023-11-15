package com.example.projetuqac.db

data class Posts(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
{
    override fun toString(): String {
        return "userId: $userId, id: $id, title: $title, body: $body"
    }
}