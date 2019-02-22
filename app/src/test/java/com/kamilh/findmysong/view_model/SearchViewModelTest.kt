package com.kamilh.findmysong.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kamilh.findmysong.di.AppEventBus
import com.kamilh.findmysong.extensions.mock
import com.kamilh.findmysong.extensions.whenever
import com.kamilh.findmysong.interactors.SearchSongs
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.utils.ResourceProvider
import com.kamilh.findmysong.utils.RxSchedulers
import com.kamilh.findmysong.utils.any
import com.kamilh.findmysong.views.search.SearchViewModel
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.verify

class SearchViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val searchSongs = mock<SearchSongs>()
    private val rxSchedulers = mock<RxSchedulers>()
    private val resourceProvider = mock<ResourceProvider>()
    private val appEventBus = mock<AppEventBus>()

    private val searchViewModel by lazy { SearchViewModel(searchSongs, rxSchedulers, resourceProvider, appEventBus) }

    @Test
    fun `SearchViewModel test if isLoading returns true`() {
        val observer = mock<Observer<Boolean>>()

        whenever(searchSongs.invoke(any()))
            .thenReturn(Single.just(Resource.Data(listOf())))
        whenever(rxSchedulers.main)
            .thenReturn(TestScheduler())
        whenever(rxSchedulers.network)
            .thenReturn(TestScheduler())

        searchViewModel.onQuery("foo")

        searchViewModel.isLoading.observeForever(observer)

        verify(observer).onChanged(true)
    }

    @Test
    fun `SearchViewModel test if changes title on new query`() {
        val observer = mock<Observer<String>>()

        whenever(searchSongs.invoke(any()))
            .thenReturn(Single.just(Resource.Data(listOf())))
        whenever(rxSchedulers.main)
            .thenReturn(TestScheduler())
        whenever(rxSchedulers.network)
            .thenReturn(TestScheduler())

        searchViewModel.onQuery("foo")

        searchViewModel.title.observeForever(observer)

        verify(observer).onChanged("foo")
    }
}
