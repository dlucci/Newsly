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
                      var multimedia : Array<Multimedia>? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TopStories

        if (id != other.id) return false
        if (section != other.section) return false
        if (title != other.title) return false
        if (url != other.url) return false
        if (byline != other.byline) return false
        if (multimedia != null) {
            if (other.multimedia == null) return false
            if (!multimedia.contentEquals(other.multimedia)) return false
        } else if (other.multimedia != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (section?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + url.hashCode()
        result = 31 * result + (byline?.hashCode() ?: 0)
        result = 31 * result + (multimedia?.contentHashCode() ?: 0)
        return result
    }
}

@Entity
data class Multimedia(@PrimaryKey var url : String = "",
                      var height : Int = -1,
                      var width : Int = -1)


data class Results(var results : Array<TopStories> = emptyArray()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Results

        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode() = results.contentHashCode()

}

class Converters {

        @TypeConverter
        fun fromMultimedia(multimedia: Array<Multimedia>?) : String{
            return Gson().toJson(multimedia)
        }

        @TypeConverter
        fun fromArray(str : String) : Array<Multimedia>? {
            val listType = object : TypeToken<Array<Multimedia>?>() {}.type
            return Gson().fromJson(str, listType)
        }
}