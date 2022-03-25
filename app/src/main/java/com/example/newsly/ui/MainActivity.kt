package com.example.newsly.ui

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsly.BuildConfig
import com.example.newsly.R
import com.example.newsly.adapter.TopStoriesAdapter
import com.example.newsly.model.results
import com.example.newsly.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer



class MainActivity : BaseActivity() {

    val model : MainViewModel by viewModels()

    override fun setUpToolbar() {
        toolbar.title = BuildConfig.APPLICATION_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
    }

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.newsObserver.observe(this, Observer { initRecyclerView(it) })

        model.getStories()
    }

    private fun initRecyclerView(stories: results) {
        recylver.layoutManager = LinearLayoutManager(this)
        val adapter = TopStoriesAdapter(stories)
        recylver.adapter = adapter

    }
}

