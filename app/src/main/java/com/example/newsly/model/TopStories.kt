package com.example.newsly.model

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class TopStories(@PrimaryKey(autoGenerate = true) var id : Long = 0,
                      var section : String? = "",
                      var title : String? = "",
                      var abstract : String? = "",
                      var url : String = "",
                      var byline : String? = "",
                      var multimedia : Array<Multimedia>? = emptyArray())

@Entity
data class Multimedia(@PrimaryKey var url : String = "",
                      var height : Int = -1,
                      var width : Int = -1)


data class Results(var results : Array<TopStories> = emptyArray())

class Converters {

        @TypeConverter
        fun fromMultimedia(multimedia: Array<Multimedia>) : String{
            return Gson().toJson(multimedia)
        }

        @TypeConverter
        fun fromArray(str : String) : Array<Multimedia> {
            val listType = object : TypeToken<Array<Multimedia>>() {}.type
            return Gson().fromJson(str, listType)
        }
}