package com.example.newsly.model

import androidx.room.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Entity
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class TopStories(@PrimaryKey(autoGenerate = true) var id : Long = 0,
                      var section : String? = "",
                      var title : String? = "",
                      var url : String = "",
                      var byline : String? = "",
                      var multimedia : List<Multimedia>? = null)

@Entity
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Multimedia(@PrimaryKey var url : String = "",
                      var height : Int = -1,
                      var width : Int = -1)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Results(var results : List<TopStories> = emptyList())

class Converters {

        @TypeConverter
        fun fromMultimedia(multimedia: List<Multimedia>?) : String{
            return Json.encodeToString(multimedia)
        }

        @TypeConverter
        fun fromArray(str : String) : List<Multimedia>? {
            return Json.decodeFromString(str)
        }
}