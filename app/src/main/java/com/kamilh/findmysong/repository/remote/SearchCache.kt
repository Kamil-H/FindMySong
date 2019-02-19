package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCache @Inject constructor() {
    private val cache = HashMap<String, List<Song>>()

    fun get(query: String) = cache[query]

    fun set(query: String, results: List<Song>) {
        cache[query] = results
    }
}
