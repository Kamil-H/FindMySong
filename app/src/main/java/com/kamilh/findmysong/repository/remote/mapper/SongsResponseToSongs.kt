package com.kamilh.findmysong.repository.remote.mapper

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.extensions.toOffsetDateTime
import com.kamilh.findmysong.repository.Mapper
import com.kamilh.findmysong.repository.remote.model.SongResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongsResponseToSongs @Inject constructor() : Mapper<SongResponse, Song> {
    override fun map(from: SongResponse): Song {
        return Song(
            title = from.trackName,
            artist = from.artistName,
            releaseYear = from.releaseDate.toOffsetDateTime()?.year,
            imageUrl = from.artworkUrl100
        )
    }
}
