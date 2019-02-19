package com.kamilh.findmysong.repository.file.model

import com.google.gson.annotations.SerializedName

data class FileSong(
    @SerializedName("Song Clean")
    val songName: String,
    @SerializedName("ARTIST CLEAN")
    val artistName: String,
    @SerializedName("Release Year")
    val releaseYear: String
)
