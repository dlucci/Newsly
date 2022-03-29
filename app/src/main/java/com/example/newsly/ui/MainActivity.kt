package com.example.newsly.ui

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsly.BuildConfig
import com.example.newsly.adapter.TopStoriesAdapter
import com.example.newsly.model.results
import com.example.newsly.viewmodel.MainViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.newsly.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    private val viewModel : MainViewModel by viewModels()

    lateinit var viewBinding: ActivityMainBinding

    fun setUpToolbar() {
        viewBinding.toolbar.title = BuildConfig.APPLICATION_TITLE
        viewBinding.toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(viewBinding.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setUpToolbar()

        viewModel.newsObserver.observe(this, Observer { initRecyclerView(it) })

        viewModel.getStories()
    }

    private fun initRecyclerView(stories: results) {
        viewBinding.recylver.layoutManager = LinearLayoutManager(this)
        val adapter = TopStoriesAdapter(stories)
        viewBinding.recylver.adapter = adapter

    }
}

