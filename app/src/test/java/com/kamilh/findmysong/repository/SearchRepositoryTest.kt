package com.kamilh.findmysong.repository

import com.kamilh.findmysong.extensions.mock
import com.kamilh.findmysong.extensions.whenever
import com.kamilh.findmysong.repository.remote.ITunesApi
import com.kamilh.findmysong.repository.remote.SearchCache
import com.kamilh.findmysong.repository.remote.SearchRemoteRepository
import com.kamilh.findmysong.repository.remote.mapper.SongsResponseToSongs
import com.kamilh.findmysong.utils.any
import com.kamilh.findmysong.utils.remoteList
import com.kamilh.findmysong.utils.searchResponse
import io.reactivex.Single
import org.junit.Test

class SearchRepositoryTest {

    private val iTunesApi = mock<ITunesApi>()
    private val searchCache = mock<SearchCache>()
    private val songsResponseToSongs = mock<SongsResponseToSongs>()

    private val searchRepository by lazy { SearchRemoteRepository(iTunesApi, searchCache, songsResponseToSongs) }
    
    @Test
    fun `SearchRepository test when iTunesApi returns value`() {
        whenever(iTunesApi.searchSongs(any()))
            .thenReturn(Single.just(searchResponse))
        whenever(songsResponseToSongs.map(any()))
            .thenReturn(remoteList[0])

        searchRepository.search("")
            .test()
            .assertValue { it is Resource.Data }
    }
}
