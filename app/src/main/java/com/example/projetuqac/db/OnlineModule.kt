package com.example.projetuqac.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnlineModule {

    @Provides
    @Named("BASE_URL")
    fun getBaseUrl(): String {
        return "https://jsonplaceholder.typicode.com/"
    }

    @Provides
    @Named("CONVERTER_FACTORY")
    fun getGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("CLIENT")
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiInterface: ApiInterface, postDao: ApiDao): ApiRepository {
        return ApiRepositoryImpl(apiInterface, postDao)
    }

    @Singleton
    @Provides
    fun getUserApiService(@Named("BASE_URL") baseUrl: String, @Named("CLIENT") client : OkHttpClient,
                          @Named("CONVERTER_FACTORY") factory: GsonConverterFactory): ApiInterface {
        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(factory)
            .build()
            .create(ApiInterface::class.java)
    }

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