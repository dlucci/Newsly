package com.example.newsly.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsly.BuildConfig
import com.example.newsly.R
import com.example.newsly.adapter.TopStoriesAdapter
import com.example.newsly.model.Results
import com.example.newsly.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsly.model.Multimedia


class MainActivity : BaseActivity() {

    val model : MainViewModel by viewModels()

    override fun setUpToolbar() {
        toolbar.title = BuildConfig.APPLICATION_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
    }

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            createRecyclerView()
        }
    }

    @Composable
    fun createRecyclerView() {

        val stories : Results? by model.resultList.observeAsState()
        model.getStories()
        LazyColumn(Modifier.fillMaxWidth()) {
            stories?.results?.forEach {
                item(it) {
                    Text(it.title ?: "")
                }
            }
        }
    }

}

