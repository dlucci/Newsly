package com.example.newsly.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var newsRepository: NewsRepository = NewsRepository()

    init {
        viewModelScope.launch {
            queryDb()
            getStories()
        }
    }

    private val articleList = mutableStateListOf<TopStories>()

    val publicArticles: List<TopStories>
        get() = articleList


    private fun getStories() {
        viewModelScope.launch {
            val stories = newsRepository.getNewsStories()
            saveInDb(stories)
            queryDb()
        }
    }

    private suspend fun saveInDb(results: Results) {
        results.results.forEach {
            NewslyDatabase.getDatabase(getApplication())
                .topStoriesDao()
                .insertStory(it)
        }
    }

    private suspend fun queryDb() {
        val stories = NewslyDatabase.getDatabase(getApplication())
            .topStoriesDao()
            .getStories()

        articleList.clear()
        articleList.addAll(stories)

    }

}