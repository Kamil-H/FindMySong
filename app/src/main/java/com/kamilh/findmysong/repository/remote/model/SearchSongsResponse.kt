package com.kamilh.findmysong.repository.remote.model

data class SearchSongsResponse(
    val resultCount: Int,
    val results: List<SongResponse>
)

data class SongResponse(
    val artistName: String,
    val trackName: String,
    val artworkUrl100: String,
    val releaseDate: String
)
