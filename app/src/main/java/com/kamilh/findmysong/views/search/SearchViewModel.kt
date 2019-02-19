package com.kamilh.findmysong.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kamilh.findmysong.R
import com.kamilh.findmysong.base.BaseViewModel
import com.kamilh.findmysong.data.Alert
import com.kamilh.findmysong.data.Loading
import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.di.AppEventBus
import com.kamilh.findmysong.extensions.plusAssign
import com.kamilh.findmysong.interactors.SearchSongs
import com.kamilh.findmysong.repository.RepositoryError
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.utils.ResourceProvider
import com.kamilh.findmysong.utils.RxSchedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchSongs: SearchSongs,
    private val rxSchedulers: RxSchedulers,
    private val resourceProvider: ResourceProvider,
    private val appEventBus: AppEventBus
) : BaseViewModel() {

    private val _list = MutableLiveData<List<SongViewState>>()

    val list: LiveData<List<SongViewState>> = _list

    init {
        search(SearchParams(query = Query.Text("jack johnson"), source = Source.All))
    }

    fun search(searchParams: SearchParams) {
        compositeDisposable += searchSongs.invoke(searchParams)
            .observeOn(rxSchedulers.main)
            .subscribeOn(rxSchedulers.network)
            .doOnSubscribe { appEventBus.onNext(Loading(true)) }
            .doOnSuccess { result ->
                when (result) {
                    is Resource.Data -> _list.value = result.result.map(this::mapToState)
                    is Resource.Error -> appEventBus.onNext(handle(result.repositoryError))
                }
                appEventBus.onNext(Loading(false))
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

    private fun handle(repositoryError: RepositoryError) = Alert(
        title = resourceProvider.getString(R.string.ErrorTitle),
        message = repositoryError.message(resourceProvider)
    ).addOkAction {  }

    fun itemClicked(position: Int) {

    }
}
