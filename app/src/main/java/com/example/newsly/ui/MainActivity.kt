package com.example.newsly.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.newsly.BuildConfig
import com.example.newsly.viewmodel.MainViewModel
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsly.model.TopStories


class MainActivity : BaseActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { fetchStories() }
    }

    @Composable
    fun fetchStories() {

        if(viewModel.publicArticles.isNotEmpty()) {
            LazyColumn{
                items(viewModel.publicArticles) { data ->
                    articleRow(data)
                }
            }
        }
        else {
            Text(text = "Loading...")
        }
    }

    @Composable
    fun articleRow(story : TopStories) {

        val multimedia = story.multimedia?.first()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    onClick = {
                              val i = Intent(this, ArticleActivity::class.java)
                              i.putExtra("url", story.url)
                              startActivity(i)
                    }
                )
            ) {
                AsyncImage(
                    model =  multimedia?.url,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp, 140.dp)
                )
                Text(
                    text = story.title ?: "",
                    )
            }


    }
}

