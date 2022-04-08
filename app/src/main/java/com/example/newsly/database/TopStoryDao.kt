package com.example.newsly.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsly.model.TopStories
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface TopStoryDao {

    @Query("SELECT * from TopStories ORDER BY TopStories.id")
    fun getStories() : Observable<Array<TopStories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story : TopStories) : Completable
}