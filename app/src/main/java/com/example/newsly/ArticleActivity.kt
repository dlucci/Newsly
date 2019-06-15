package com.example.newsly

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : Activity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_article)
         if(intent.hasExtra("url"))
             webview.loadUrl(intent.getStringExtra("url"))
    }

}