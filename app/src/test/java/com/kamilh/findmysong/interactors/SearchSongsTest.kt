package com.kamilh.findmysong.interactors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kamilh.findmysong.extensions.mock
import com.kamilh.findmysong.extensions.whenever
import com.kamilh.findmysong.repository.RepositoryError
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.utils.any
import com.kamilh.findmysong.utils.localList
import com.kamilh.findmysong.utils.remoteList
import com.kamilh.findmysong.views.search.Query
import com.kamilh.findmysong.views.search.SearchParams
import com.kamilh.findmysong.views.search.Source
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SearchSongsTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val localSearch = mock<LocalSearch>()
    private val remoteSearch = mock<RemoteSearch>()

    private val searchSongs by lazy { SearchSongs(localSearch, remoteSearch) }

    @Test
    fun `SearchSong test when Query = All, Source = All and returns value`() {
        whenever(remoteSearch.invoke(any()))
            .thenReturn(Single.just(Resource.Data(remoteList)))
        whenever(localSearch.invoke(any()))
            .thenReturn(Single.just(Resource.Data(localList)))

        searchSongs.invoke(SearchParams(query = Query.All, source = Source.All))
            .test()
            .assertValue { (it as? Resource.Data)?.result?.size == (remoteList.size + localList.size) }
    }

    @Test
    fun `SearchSong test when Query = All, Source = All and Remote returns Error`() {
        val response = Resource.Error(RepositoryError.HttpError.InternalServerErrorException)

        whenever(remoteSearch.invoke(any()))
            .thenReturn(Single.just(response))
        whenever(localSearch.invoke(any()))
            .thenReturn(Single.just(Resource.Data(listOf())))

        searchSongs.invoke(SearchParams(query = Query.All, source = Source.All))
            .test()
            .assertValue(response)
    }

    @Test
    fun `SearchSong test when Query = All, Source = All and Local returns Error`() {
        val response = Resource.Error(RepositoryError.DiscError)

        whenever(remoteSearch.invoke(any()))
            .thenReturn(Single.just(Resource.Data(listOf())))
        whenever(localSearch.invoke(any()))
            .thenReturn(Single.just(response))

        searchSongs.invoke(SearchParams(query = Query.All, source = Source.All))
            .test()
            .assertValue(response)
    }
}
