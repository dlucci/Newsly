package com.example.newsly.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class TopStories(@PrimaryKey(autoGenerate = true) var id : Long = 0,
                      var section : String? = "",
                      var title : String? = "",
                      var url : String = "",
                      var byline : String? = "",
                      var multimedia : List<Multimedia>? = null)

@Entity
data class Multimedia(@PrimaryKey var url : String = "",
                      var height : Int = -1,
                      var width : Int = -1)


data class Results(var results : List<TopStories> = emptyList())

class Converters {

        @TypeConverter
        fun fromMultimedia(multimedia: List<Multimedia>?) : String{
            return Gson().toJson(multimedia)
        }

        @TypeConverter
        fun fromArray(str : String) : List<Multimedia>? {
            val listType = object : TypeToken<List<Multimedia>?>() {}.type
            return Gson().fromJson(str, listType)
        }
}