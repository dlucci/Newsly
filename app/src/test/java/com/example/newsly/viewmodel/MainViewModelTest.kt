package com.example.newsly.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.newsly.model.TopStories
import com.example.newsly.model.Results
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel : MainViewModel

    private lateinit var results: Results

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MainViewModel()

        createResults()
    }

    private fun createResults() {
        val topStories = ArrayList<TopStories>()
        topStories.add(TopStories(section = "foo", title = "bar", url = "bizz", byline = "hoo", multimedia = null))
        results.results = topStories.toArray(TopStories::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getStories() {

        val emptyObserver = Observer<Results> {}
        //viewModel.newsObserver.observeForever(emptyObserver)

        assertTrue(viewModel != null)
    }
}