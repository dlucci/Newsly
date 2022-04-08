package com.example.newsly.repository

import com.example.newsly.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository {

    var nyService: NYService

    init {
        var builder = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        nyService = builder.create(NYService::class.java)
    }


    fun getNewsStories() = nyService.getArticles("W8bHOzqnSaeacXqa9xHnMkz93qKmb0sM")
}