package com.example.newsly.ui

import android.net.Uri
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun StoryViewer(navController: NavHostController, url: String) {
    val decodedUrl = Uri.decode(url)
    AndroidView(
        factory = {
            WebView(it).apply {
                loadUrl(decodedUrl)
            }
        }
    )
}