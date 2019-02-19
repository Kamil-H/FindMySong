package com.kamilh.findmysong.views.search

import android.util.Log
import com.kamilh.findmysong.base.BaseViewModel
import com.kamilh.findmysong.extensions.plusAssign
import com.kamilh.findmysong.interactors.SearchSongs
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.utils.ResourceProvider
import com.kamilh.findmysong.utils.RxSchedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchSongs: SearchSongs,
    private val rxSchedulers: RxSchedulers,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    init {
        compositeDisposable += searchSongs.invoke(SearchParams(query = Query.Text("jack johnson"), source = Source.All))
            .observeOn(rxSchedulers.main)
            .subscribeOn(rxSchedulers.network)
            .doOnSuccess { result ->
                Log.i("SearchViewModel", when (result) {
                    is Resource.Data -> result.result.size.toString()
                    is Resource.Error -> result.repositoryError.message(resourceProvider)
                })
            }
            .subscribe()
    }
}
