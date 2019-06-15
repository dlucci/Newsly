package com.example.newsly.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setUpToolbar()
    }

    abstract fun setUpToolbar()

    abstract fun getLayout(): Int
}