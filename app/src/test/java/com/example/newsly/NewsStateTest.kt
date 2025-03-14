package com.example.newsly

import com.example.newsly.model.TopStories
import com.example.newsly.viewmodel.NewsState
import junit.framework.TestCase.assertEquals
import org.junit.Test

class NewsStateTest {

    @Test
    fun `test RemoteSuccess state holds correct data`() {
        val topStories = listOf(TopStories("Title1"), TopStories("Title2"))
        val remoteSuccessState = NewsState.RemoteSuccess(topStories)

        // Verify that the RemoteSuccess state holds the expected top stories
        assertEquals(topStories, remoteSuccessState.topStories)
    }

    @Test
    fun `test LocalSuccess state holds correct data`() {
        val topStories = listOf(TopStories("Title1"), TopStories("Title2"))
        val localSuccessState = NewsState.LocalSuccess(topStories)

        // Verify that the LocalSuccess state holds the expected top stories
        assertEquals(topStories, localSuccessState.topStories)
    }

    @Test
    fun `test Error state holds correct data`() {
        val message = "Network Error"
        val topStories = listOf(TopStories("Title1"), TopStories("Title2"))
        val errorState = NewsState.Error(message, topStories)

        // Verify that the Error state holds the expected message and top stories
        assertEquals(message, errorState.message)
        assertEquals(topStories, errorState.topStories)
    }

    @Test
    fun `test Loading state holds correct value`() {
        val isLoading = true
        val loadingState = NewsState.Loading(isLoading)

        // Verify that the Loading state holds the expected isLoading value
        assertEquals(isLoading, loadingState.isLoading)
    }

}