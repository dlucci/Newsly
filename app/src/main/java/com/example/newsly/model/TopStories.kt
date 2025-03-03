package com.example.newsly.model

import androidx.room.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Entity
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class TopStories(var section : String? = "",
                      @PrimaryKey var title : String = "",
                      var url : String = "",
                      var byline : String? = "",
                      var multimedia : List<Multimedia>? = null,
                      @SerialName("updated_date") var updatedDate : String? = "")

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