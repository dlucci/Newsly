package com.example.newsly.repository

import com.example.newsly.model.Results
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository(nyService: NYService) {

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