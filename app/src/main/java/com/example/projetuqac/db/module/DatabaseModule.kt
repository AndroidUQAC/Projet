package com.example.projetuqac.db.module

import android.content.Context
import androidx.room.Room
import com.example.projetuqac.db.api.ApiDao
import com.example.projetuqac.db.PostsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): PostsDatabase {
        return Room.databaseBuilder(
            context,
            PostsDatabase::class.java,
            "Posts.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePostDao(postsDatabase: PostsDatabase): ApiDao = postsDatabase.postDao()

}