package com.example.newsly.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.newsly.viewmodel.MainViewModel
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsly.model.Multimedia
import com.example.newsly.model.TopStories


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { fetchStories() }
    }

    @Composable
    fun fetchStories() {

        if (viewModel.publicArticles.isNotEmpty()) {

            LazyColumn {

                items(viewModel.publicArticles) {
                    articleRow(story = it)
                }
            }

        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading...")
            }
        }
    }

    @Composable
    fun articleRow(story: TopStories) {

        val multimedia = story.multimedia?.first()
        Box(
            modifier = Modifier
                .clickable(
                    onClick = {
                        val nytIntent = Uri.parse(story.url)
                            .let {
                                Intent(Intent.ACTION_VIEW, it)
                            }
                        startActivity(nytIntent)
                    }
                )
                .background(
                    color = Color(0xFFFFFFFF)
                )
                .fillMaxWidth()
        ) {


            AsyncImage(
                model = multimedia?.url,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()

            )

            Text(
                text = story.title ?: "",
                fontFamily = NYTFont,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Text(
                text = story.byline ?: "",
                fontFamily = NYTFont,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }

    @Preview
    @Composable
    fun previewRow() {

        val multimedia = Multimedia(
            url = "https://www.nytimes.com/live/2022/03/31/business/economy-news-opec-inflation"
        )

        val topStories = TopStories(
            title = "Foo Bar:  A quest",
            byline = "by Mike Adams",
            abstract = "In a world filled with torment, one man stands alone by the foo bar",
            multimedia = arrayOf(multimedia)
        )

        articleRow(story = topStories)
    }

}

