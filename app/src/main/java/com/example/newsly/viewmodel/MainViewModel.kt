package com.example.newsly.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


sealed class NewsState {
    data class RemoteSuccess(val topStories: List<TopStories>) : NewsState()
    data class LocalSuccess(val topStories: List<TopStories>) : NewsState()
    data class Error(val message: String, val topStories: List<TopStories>) : NewsState()
    data class Loading(val isLoading: Boolean) : NewsState()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val newsRepository: NewsRepository,
    private val database: NewslyDatabase,
    val queryDb: Flow<List<TopStories>> = database.topStoriesDao().getStories().distinctUntilChanged()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsState>(NewsState.Loading(false))
    val uiState: StateFlow<NewsState> = _uiState

    @VisibleForTesting
    suspend fun saveInDb(results: Results) {
        val dao = database.topStoriesDao()
        dao.insertStories(results.results)
    }


    init {
        updateState(NewsState.Loading(true))
        viewModelScope.launch {
            val networkFlow = newsRepository.getNewsStories
            val dbFlow = queryDb

            dbFlow.flatMapConcat { dbData ->
                flow {
                    emit(NewsState.Loading(false))
                    emit(NewsState.LocalSuccess(dbData))
                    networkFlow.collect { data ->
                        if (data.isSuccess) {
                            val results = data.getOrNull()
                            if (results != null) {
                                saveInDb(results)
                                emit(NewsState.RemoteSuccess(results.results))
                            }
                        } else {
                            val exception = data.exceptionOrNull()
                            emit(NewsState.Error(exception?.message ?: "Unknown error", dbData))
                        }
                    }
                }
            }.catch {
                emit(NewsState.Error(it.message ?: "Unknown error", emptyList()))
            }.collect {
                updateState(it)
            }
        }
    }

    @VisibleForTesting
    fun updateState(newState: NewsState) {
        _uiState.value = newState
    }
}