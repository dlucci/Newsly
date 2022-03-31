package com.example.newsly.ui

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsly.model.Multimedia
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
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Loading...")
            }
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
                ).background(
                    color = Color(0xFFFFFFFF)
                )
            ) {
                AsyncImage(
                    model =  multimedia?.url,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp, 140.dp)
                )
                Column {
                    Text(
                        text = story.title ?: "",
                        fontFamily = NYTFont,
                        fontSize = 20.sp
                    )
                    Text(
                        text = story.byline ?: "",
                        fontFamily = NYTFont,
                        color = Color.Gray,
                        fontSize = 15.sp

                    )
                    Text(
                        text = story.abstract ?: "",
                        fontFamily = NYTFont,
                        fontSize = 12.sp
                    )
                }
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

