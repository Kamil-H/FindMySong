package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.repository.remote.model.SearchSongsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("search")
    fun searchSongs(
        @Query("term") query: String
    ): Single<SearchSongsResponse>
}
