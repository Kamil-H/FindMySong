package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.data.Song
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCache @Inject constructor() {
    private val cache = HashMap<String, List<Song>>()

    fun get(query: String): Maybe<List<Song>> {
        val value = cache[query]
        return if (value != null) {
            Maybe.defer {
                MaybeSource { value }
            }
        } else {
            Maybe.empty()
        }
    }

    fun set(query: String, results: List<Song>) {
        cache[query] = results
    }
}
