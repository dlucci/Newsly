package com.example.newsly.ui

import android.graphics.Color
import android.os.Bundle
import com.example.newsly.BuildConfig
import com.example.newsly.R
import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : BaseActivity() {
    override fun setUpToolbar() {
        toolbar.title = BuildConfig.APPLICATION_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
    }

    override fun getLayout() = R.layout.activity_article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         if(intent.hasExtra("url"))
             webview.loadUrl(intent.getStringExtra("url"))
    }

}