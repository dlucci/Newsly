package com.example.newsly.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsly.model.results
import com.example.newsly.repository.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel() : ViewModel() {

    val newsRepository = NewsRepository()

    var newsObserver = MutableLiveData<results>()

    fun getStories() {
        newsRepository.getNewsStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stories ->
                    newsObserver.value = stories
                },
                {
                    error -> Log.d("EIFLE", error?.message ?: "")
                })
    }

}