package com.example.newsly.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch


sealed class NewsState {
    data class RemoteSuccess(val topStories: Array<TopStories>) : NewsState()
    data class LocalSuccess(val topStories: Array<TopStories>) : NewsState()
    data class Error(val message: String, val code: Int) : NewsState()
    data class Loading(val isLoading: Boolean) : NewsState()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var newsRepository: NewsRepository = NewsRepository()

    private val _uiState = MutableStateFlow<NewsState>(NewsState.Loading(false))
    val uiState: StateFlow<NewsState> = _uiState

    init {
        updateState(NewsState.Loading(true))
        viewModelScope.launch {
            val networkFlow = newsRepository.getNewsStories
            val dbFlow = queryDb ?: flowOf(emptyArray())
            dbFlow.flatMapConcat { dbData ->
                flow {
                    val safeDbData = dbData ?: emptyArray() // Ensure it's never null
                    emit(safeDbData)
                    updateState(NewsState.LocalSuccess(safeDbData))

                    val networkData = networkFlow.first()
                    saveInDb(networkData)
                    emit(networkData.results)
                    updateState(NewsState.RemoteSuccess(networkData.results))
                }
            }.collect {
                updateState(NewsState.Loading(false))
            }
        }
    }

    fun updateState(newState: NewsState) {
        _uiState.value = newState
    }


    private suspend fun saveInDb(results: Results) {
        results.results.forEach {
            NewslyDatabase.getDatabase(getApplication())
                .topStoriesDao()
                .insertStory(it)
        }
    }

    private val queryDb: Flow<Array<TopStories>> =
        NewslyDatabase.getDatabase(getApplication())
            .topStoriesDao()
            .getStories()


}