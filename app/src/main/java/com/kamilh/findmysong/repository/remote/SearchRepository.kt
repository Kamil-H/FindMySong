package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import io.reactivex.Single

interface SearchRepository {
    fun search(query: String): Single<Resource<List<Song>>>
}
