package com.kamilh.findmysong.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kamilh.findmysong.base.BaseViewModel
import com.kamilh.findmysong.data.Song
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

    private val _list = MutableLiveData<List<SongViewState>>()

    val list: LiveData<List<SongViewState>> = _list

    init {
        compositeDisposable += searchSongs.invoke(SearchParams(query = Query.Text("jack johnson"), source = Source.All))
            .observeOn(rxSchedulers.main)
            .subscribeOn(rxSchedulers.network)
            .doOnSuccess { result ->
                when (result) {
                    is Resource.Data -> _list.value = result.result.map(this::mapToState)
                    is Resource.Error -> result.repositoryError.message(resourceProvider)
                }
            }
            .subscribe()
    }

    private fun mapToState(song: Song) = SongViewState(
        title = song.title,
        subtitle = song.artist,
        smallText = song.releaseYear?.toString() ?: "-",
        imageUrl = song.imageUrl,
        isRightCornerImage = true
    )

    fun itemClicked(position: Int) {

    }
}
