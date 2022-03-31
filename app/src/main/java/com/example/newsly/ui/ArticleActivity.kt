package com.example.newsly.ui

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

class ArticleActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            loadWebView(intent.getStringExtra("url"))
        }
    }


    @Composable
    private fun loadWebView(url: String?) {

        AndroidView(factory = {
            WebView(this).apply {
                webViewClient = WebViewClient()

                loadUrl(url ?: "")
            }
        })
    }


}