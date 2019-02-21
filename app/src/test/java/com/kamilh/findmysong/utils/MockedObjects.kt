package com.kamilh.findmysong.utils

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.remote.model.SearchSongsResponse
import com.kamilh.findmysong.repository.remote.model.SongResponse

val searchResponse = SearchSongsResponse(
    resultCount = 2,
    results = listOf(
        SongResponse(
            artistName = "Riss",
            trackName = "12:14",
            artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music114/v4/68/d9/99/68d99989-61bd-97ff-be99-3c0ec80b7183/source/100x100bb.jpg",
            releaseDate = "2005-03-01T08:00:00Z"
        ),
        SongResponse(
            artistName = "Rupert Wyatt",
            trackName = "Rise of the Planet of the Apes",
            artworkUrl100 = "https://is1-ssl.mzstatic.com/image/thumb/Video122/v4/da/0c/bb/da0cbb64-9eb2-c1da-bcf5-6ea0cb7ebd9e/source/100x100bb.jpg",
            releaseDate = "2005-03-01T08:00:00Z"
        )
    )
)

val remoteList = listOf(
    Song(
        title = "12:14",
        artist = "Riss",
        imageUrl = "https://is2-ssl.mzstatic.com/image/thumb/Music114/v4/68/d9/99/68d99989-61bd-97ff-be99-3c0ec80b7183/source/100x100bb.jpg",
        releaseYear = 2019
    ),
    Song(
        title = "Rise of the Planet of the Apes",
        artist = "Rupert Wyatt",
        imageUrl = "https://is1-ssl.mzstatic.com/image/thumb/Video122/v4/da/0c/bb/da0cbb64-9eb2-c1da-bcf5-6ea0cb7ebd9e/source/100x100bb.jpg",
        releaseYear = 2011
    )
)

val localList = listOf(
    Song(
        title = "You Oughta Know",
        artist = "Alanis Morissette",
        imageUrl = null,
        releaseYear = 1995
    ),
    Song(
        title = "You Oughta Know",
        artist = "Alanis Morissette",
        imageUrl = null,
        releaseYear = 1995
    )
)
