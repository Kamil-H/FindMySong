package com.kamilh.findmysong.repository.file

import com.kamilh.findmysong.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongCache @Inject constructor() {
    private var cache: List<Song>? = null

    fun get() = cache

    fun set(results: List<Song>) {
        cache = results
    }
}
