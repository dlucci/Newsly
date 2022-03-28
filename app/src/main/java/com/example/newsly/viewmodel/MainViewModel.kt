package com.example.newsly.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsly.model.Results
import com.example.newsly.repository.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel() : ViewModel() {

    val newsRepository = NewsRepository()

    private var newsObserver = MutableLiveData<Results>()

    var resultList = MutableLiveData<Results>()
//        get() = newsObserver
//        set(value) {
//            field.value = value.value
//        }

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