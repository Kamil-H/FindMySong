package com.kamilh.findmysong.repository.file.mapper

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Mapper
import com.kamilh.findmysong.repository.file.model.FileSong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSongToSong @Inject constructor() : Mapper<FileSong, Song> {
    override fun map(from: FileSong): Song {
        return Song(
            title = from.songName,
            artist = from.artistName,
            releaseYear = from.releaseYear.toIntOrNull()
        )
    }
}
