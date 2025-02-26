package com.example.newsly.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


sealed class NewsState {
    data class RemoteSuccess(val topStories: Array<TopStories>) : NewsState()
    data class LocalSuccess(val topStories: Array<TopStories>) : NewsState()
    data class Error(val message: String) : NewsState()
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
                            emit(NewsState.Error(exception?.message ?: "Unknown error"))
                            queryDb.collect {
                                emit(NewsState.LocalSuccess(it))
                            }
                        }
                    }

                }
            }.catch {
                emit(NewsState.Error(it.message ?: "Unknown error"))
            }.collect {
                updateState(it)
            }
        }
    }

    private fun updateState(newState: NewsState) {
        _uiState.value = newState
    }


    private suspend fun saveInDb(results: Results) {
        val dao = NewslyDatabase.getDatabase(getApplication()).topStoriesDao()
        dao.insertStories(results.results)
    }

    private val queryDb: Flow<Array<TopStories>> =
        NewslyDatabase.getDatabase(getApplication())
            .topStoriesDao()
            .getStories()


}