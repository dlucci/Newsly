package com.example.newsly.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.example.newsly.model.TopStories
import com.example.newsly.model.results
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel : MainViewModel

    lateinit var results: results

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MainViewModel()

        createResults()
    }

    private fun createResults() {
        var topStories = ArrayList<TopStories>()
        topStories.add(TopStories(section = "foo", title = "bar", abstract = "buzz", url = "bizz", byline = "hoo", multimedia = null))
        results.results = topStories.toArray(TopStories::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getStories() {

        val emptyObserver = Observer<results> {}
        viewModel.newsObserver.observeForever(emptyObserver)

        val

        assertTrue(viewModel != null)
    }
}