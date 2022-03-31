package com.example.newsly.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel() : ViewModel() {
     private var newsRepository : NewsRepository = NewsRepository()

    init {
        getStories()
    }

    private val articleList = mutableStateListOf<TopStories>()

    val publicArticles : List<TopStories>
        get() = articleList


    fun getStories() {
        newsRepository.getNewsStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stories ->
                    articleList.clear()
                    articleList.addAll(stories.results)
                },
                {
                    error -> Log.d("EIFLE", error?.message ?: "")
                })
    }

}