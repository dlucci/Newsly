package com.example.newsly.ui

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import com.example.newsly.BuildConfig
import com.example.newsly.R
import com.example.newsly.databinding.ActivityArticleBinding

class ArticleActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityArticleBinding

    fun setUpToolbar() {
        viewBinding.toolbar.title = BuildConfig.APPLICATION_TITLE
        viewBinding.toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(viewBinding.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityArticleBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        setUpToolbar()

         if(intent.hasExtra("url"))
             viewBinding.webview.loadUrl(intent?.getStringExtra("url") ?: "")
    }

}