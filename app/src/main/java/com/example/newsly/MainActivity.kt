package com.example.newsly

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsly.adapter.TopStoriesAdapter
import com.example.newsly.model.results
import com.example.newsly.repository.NYService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()

        var builder = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        var nyService = builder.create(NYService::class.java)

        nyService
            .getArticles("W8bHOzqnSaeacXqa9xHnMkz93qKmb0sM")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stories ->
                    initRecyclerView(stories)
                },
                {
                        error -> Log.d("EIFLE", error.message)
                })

    }

    private fun initToolBar() {
        toolbar.title = BuildConfig.APPLICATION_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

    }

    private fun initRecyclerView(stories: results) {
        recylver.layoutManager = LinearLayoutManager(this)
        val adapter = TopStoriesAdapter(stories)
        recylver.adapter = adapter

    }
}
