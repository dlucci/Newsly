package com.example.newsly.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsly.model.TopStories

@Dao
interface TopStoryDao {

    @Query("SELECT * from TopStories ORDER BY TopStories.id")
    suspend fun getStories() : Array<TopStories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story : TopStories)
}