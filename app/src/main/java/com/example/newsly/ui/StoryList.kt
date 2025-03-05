package com.example.newsly.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsly.model.Multimedia
import com.example.newsly.model.TopStories
import com.example.newsly.viewmodel.MainViewModel
import com.example.newsly.viewmodel.NewsState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StoryList(navController: NavController) {
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if(uiState is NewsState.Error) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = (uiState as NewsState.Error).message,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }

    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                when (uiState) {
                    is NewsState.RemoteSuccess -> {
                        val successState = uiState as NewsState.RemoteSuccess
                        ArticleList(stories = successState.topStories, navController = navController)
                    }

                    is NewsState.LocalSuccess -> {
                        val successState = uiState as NewsState.LocalSuccess
                        ArticleList(stories = successState.topStories, navController = navController)
                    }

                    is NewsState.Loading -> {
                        val loadingState = uiState as NewsState.Loading
                        Loading(loadingState.isLoading)
                    }

                    is NewsState.Error -> {
                        val errorState = uiState as NewsState.Error
                        ArticleList(stories = errorState.topStories, navController = navController)
                    }
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter))

            }
        }
    )

}

@Composable
fun Loading(isLoading: Boolean) {
    if (isLoading) {
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
fun ArticleList(stories: List<TopStories>, navController: NavController) {
    LazyColumn {
        items(stories) {
            ArticleRow(story = it, navController = navController)
        }
    }
}

@Composable
fun ArticleRow(story: TopStories, navController: NavController) {

    val multimedia = story.multimedia?.first()
    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    navController.navigate(Routes.Article.route + "/${Uri.encode(story.url)}")

//                    val nytIntent = Uri
//                        .parse(story.url)
//                        .let {
//                            Intent(Intent.ACTION_VIEW, it)
//                        }
//                    startActivity(nytIntent)
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
fun PreviewRow(navController: NavController = rememberNavController()) {

    val multimedia = Multimedia(
        url = "https://www.nytimes.com/live/2022/03/31/business/economy-news-opec-inflation"
    )

    val topStories = TopStories(
        title = "Foo Bar:  A quest",
        byline = "by Mike Adams",
        multimedia = listOf(multimedia)
    )

    ArticleRow(story = topStories, navController = navController)
}
