package com.example.newsly.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel(application) {
     private var newsRepository : NewsRepository = NewsRepository()

    private val TAG = "MainViewModel"

    init {
        queryDb()
        getStories()
    }

    private val articleList = mutableStateListOf<TopStories>()

    val publicArticles : List<TopStories>
        get() = articleList


    fun getStories() {
        compositeDisposable.add(newsRepository.getNewsStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stories ->
                    saveInDb(stories)
                    queryDb()
                },
                {
                    error -> Log.d("EIFLE", error?.message ?: "")
                }))
    }

    fun saveInDb(results: Results) {
        results.results.forEach {
            NewslyDatabase.getDatabase(getApplication())
                .topStoriesDao()
                .insertStory(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    fun queryDb() {
        compositeDisposable.add(NewslyDatabase.getDatabase(getApplication())
            .topStoriesDao()
            .getStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                articleList.clear()
                articleList.addAll(it)
            })
    }

}