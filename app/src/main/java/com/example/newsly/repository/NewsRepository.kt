package com.example.newsly.repository

import com.example.newsly.BuildConfig
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    val getNewsStories : Flow<Result<Results>>  = flow {
        val latestStories = nyService.getArticles("W8bHOzqnSaeacXqa9xHnMkz93qKmb0sM")
        if(latestStories.isSuccessful) {
            latestStories.body()?.let {
                emit(Result.success(it))
            }
        } else {
            emit(Result.failure(Throwable("${latestStories.code()}: ${latestStories.message()}")))

        }
    }
}