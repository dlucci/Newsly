package com.example.newsly.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsly.model.TopStories
import kotlinx.coroutines.flow.Flow

@Dao
interface TopStoryDao {

    @Query("SELECT * from TopStories ORDER BY TopStories.id")
    fun getStories() : Flow<List<TopStories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories : List<TopStories>)
}