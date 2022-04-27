package com.example.newsly.repository

import com.example.newsly.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository {

    private var nyService: NYService

    init {
        val builder = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nyService = builder.create(NYService::class.java)
    }


    suspend fun getNewsStories() = nyService.getArticles("W8bHOzqnSaeacXqa9xHnMkz93qKmb0sM")
}