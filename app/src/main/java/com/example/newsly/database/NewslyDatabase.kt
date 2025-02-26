package com.example.newsly.database

import android.content.Context
import androidx.room.*
import com.example.newsly.model.Converters
import com.example.newsly.model.Multimedia
import com.example.newsly.model.TopStories

@Database(entities = [TopStories::class, Multimedia::class],
    version = 1,
    exportSchema = true)
@TypeConverters(Converters::class)
abstract class NewslyDatabase : RoomDatabase() {

    abstract fun topStoriesDao() : TopStoryDao

    companion object {

        @Volatile
        private var INSTANCE : NewslyDatabase? = null

        fun getDatabase(context: Context) : NewslyDatabase{
                 return INSTANCE ?: synchronized(this) {
                     val instance = Room.databaseBuilder(
                         context.applicationContext,
                         NewslyDatabase::class.java, "newsly-database"
                     )
                         .build()
                     INSTANCE = instance
                     instance
                 }
        }
    }
}