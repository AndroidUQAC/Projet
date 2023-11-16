package com.example.projetuqac.db.data

import com.example.projetuqac.db.api.Posts

object LocalDataPosts {
    private val posts = listOf(
        Posts(
            id = 1,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 2,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 3,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 4,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 5,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 6,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 7,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 8,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 9,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 10,
            title = "title",
            body = "body",
            userId = 1
        ),
        Posts(
            id = 11,
            title = "title",
            body = "body",
            userId = 1
        ),
    )

    fun getPosts(): List<Posts> {
        return posts
    }
}