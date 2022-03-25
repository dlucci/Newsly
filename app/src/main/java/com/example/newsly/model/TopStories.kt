package com.example.newsly.model


data class TopStories( var section : String? = null,
                       var title : String? = null,
                       var abstract : String? = null,
                       var url : String? = null,
                       var byline : String? = null,
                       var multimedia : Array<Multimedia>?)

data class Multimedia(var url : String? = null,
                      var height : Int,
                      var width : Int)

data class results( var results : Array<TopStories>)