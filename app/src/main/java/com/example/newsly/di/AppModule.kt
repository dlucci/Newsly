package com.example.newsly.di

import com.example.newsly.BuildConfig
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.repository.NYService
import com.example.newsly.repository.NewsRepository
import com.example.newsly.viewmodel.MainViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val appModule = module {
    single { NewsRepository(get()) }
    single { NewslyDatabase.getDatabase(androidContext()) }
    single {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(NYService::class.java) }

    viewModel { MainViewModel(androidApplication(), get(), get()) }
}