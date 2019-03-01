package com.kamilh.findmysong.repository.database

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import io.reactivex.Single

interface Songs2Repository {
    fun songs(): Single<Resource<List<Song>>>
    fun songs(query: String): Single<Resource<List<Song>>>
}
