package com.example.newsly.repository

import com.example.newsly.model.Results
import retrofit2.http.GET
import retrofit2.http.Query

interface NYService {

    @GET("/svc/topstories/v2/home.json")
    suspend fun getArticles(@Query("api-key") key : String) : Results

}