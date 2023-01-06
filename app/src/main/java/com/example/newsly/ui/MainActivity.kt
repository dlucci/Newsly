package com.example.newsly.ui

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.example.newsly.model.Multimedia
import com.example.newsly.model.TopStories
import com.example.newsly.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { FetchStories() }
    }

    @Composable
    fun FetchStories() {

        if (viewModel.publicArticles.isNotEmpty()) {

            LazyColumn {

                items(viewModel.publicArticles) {
                    ArticleRow(story = it)
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
    fun ArticleRow(story: TopStories) {

        val multimedia = story.multimedia?.first()
        var drawable :Drawable? = null
        Box(
            modifier = Modifier
                .clickable(
                    onClick = {
                        val nytIntent = Uri
                            .parse(story.url)
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
                modifier = Modifier.fillMaxWidth(),
                onSuccess = {
                    drawable = it.result.drawable
                    createTitle()
                }
            )
            Text(
                text = story.byline ?: "",
                fontFamily = NYTFont,
                color = getAverageOffset(drawable),
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }

    @Composable
    fun createTitle() {
        Text(
            text = story.title ?: "",
            fontFamily = NYTFont,
            fontSize = 20.sp,
            ≠color = Color.White,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }

    private fun getAverageOffset(drawable: Drawable?) : Color {
        val bitmap = Bitmap.createBitmap(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        drawable?.draw(canvas)

        val palette = Palette.from(bitmap).generate()
        return Color(palette.dominantSwatch?.rgb ?: 0)
    }

    @Preview
    @Composable
    fun PreviewRow() {

        val multimedia = Multimedia(
            url = "https://static01.nyt.com/images/2022/06/14/world/14buffalo-shooting-neighborhood-segregation/merlin_206869314_d31e5711-af35-41c6-9aaf-452897b9a5c5-superJumbo.jpg"
        )

        val topStories = TopStories(
            title = "Foo Bar:  A quest",
            byline = "by Mike Adams",
            multimedia = arrayOf(multimedia)
        )

        ArticleRow(story = topStories)
    }

}

