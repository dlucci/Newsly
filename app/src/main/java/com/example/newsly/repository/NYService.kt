package com.example.newsly.repository

import com.example.newsly.model.Results
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NYService {

    @GET("/svc/topstories/v2/home.json")
    fun getArticles(@Query("api-key") key : String) : Observable<Results>

}