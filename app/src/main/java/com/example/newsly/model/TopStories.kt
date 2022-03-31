package com.example.newsly.model


data class TopStories( var section : String? = "",
                       var title : String? = "",
                       var abstract : String? = "",
                       var url : String? = "",
                       var byline : String? = "",
                       var multimedia : Array<Multimedia>? = emptyArray())

data class Multimedia(var url : String? = "",
                      var height : Int = -1,
                      var width : Int = -1)

data class Results(var results : Array<TopStories> = emptyArray())