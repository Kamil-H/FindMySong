package com.kamilh.findmysong.data

data class Song(
    val title: String,
    val artist: String,
    val releaseYear: Int?,
    val imageUrl: String? = null
)
