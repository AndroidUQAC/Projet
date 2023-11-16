package com.example.projetuqac.db.repository

import android.util.Log
import com.example.projetuqac.db.models.PostEntity
import com.example.projetuqac.db.api.Posts
import com.example.projetuqac.db.Result
import com.example.projetuqac.db.api.ApiDao
import com.example.projetuqac.db.api.ApiInterface
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val postDao: ApiDao
) : ApiRepository {
    private fun fromPost(post: Posts) = PostEntity(
        id = post.id,
        userId = post.userId,
        title = post.title,
        body = post.body
    )

    private fun toPost(postEntity: PostEntity) = Posts(
        id = postEntity.id,
        userId = postEntity.userId,
        title = postEntity.title,
        body = postEntity.body
    )

    private fun toPost(postEntity: List<PostEntity>) = postEntity.map { toPost(it) }

    override fun getPosts() = flow {
        emit(Result.Loading())
        val localPosts = postDao.getPosts()
        if (localPosts.isEmpty()) {
            Log.d("ApiRepositoryImpl", "Je vais le chercher sur internet")
            emit(Result.Loading())
            val posts = apiInterface.getPosts().body()
            postDao.insertPosts(posts!!.map { fromPost(it) })
            emit(Result.Success(posts))
        } else {
            Log.d("ApiRepositoryImpl", "Je vais le chercher dans la base de données")
            val posts = localPosts.map { toPost(it) }
            emit(Result.Success(posts))
        }
    }.catch { cause ->
        Log.d("ApiRepositoryImpl", "Je vais le chercher dans la base de données")
        val localPosts = postDao.getPosts()
        val posts = localPosts.map { toPost(it) }
        emit(Result.Error(posts, cause.message ?: "Error getting posts"))
    }
}