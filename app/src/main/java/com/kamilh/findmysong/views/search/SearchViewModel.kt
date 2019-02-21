package com.kamilh.findmysong.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kamilh.findmysong.R
import com.kamilh.findmysong.base.BaseViewModel
import com.kamilh.findmysong.data.Alert
import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.di.AppEventBus
import com.kamilh.findmysong.extensions.plusAssign
import com.kamilh.findmysong.interactors.SearchSongs
import com.kamilh.findmysong.repository.RepositoryError
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.utils.ResourceProvider
import com.kamilh.findmysong.utils.RxSchedulers
import com.kamilh.findmysong.utils.SingleLiveEvent
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchSongs: SearchSongs,
    private val rxSchedulers: RxSchedulers,
    private val resourceProvider: ResourceProvider,
    private val appEventBus: AppEventBus
) : BaseViewModel() {

    private val _list = MutableLiveData<List<SongViewState>>()
    private val _isLoading = SingleLiveEvent<Boolean>()
    private val _isEmptyView = MutableLiveData<Boolean>()
    private val _chipConfigs = MutableLiveData<SourceChipGroup.Configuration>()

    val list: LiveData<List<SongViewState>> = _list
    val isLoading: LiveData<Boolean> = _isLoading
    val isEmptyView: LiveData<Boolean> = _isEmptyView
    val chipConfigs: LiveData<SourceChipGroup.Configuration> = _chipConfigs

    private val sources = listOf(Source.Remote, Source.Local, Source.All)
    private var searchParams = SearchParams(query = Query.All, source = Source.All)

    init {
        _chipConfigs.value = SourceChipGroup.Configuration(
            list = sources,
            selectedIndex = sources.indexOf(Source.All)
        )
    }

    private fun search(searchParams: SearchParams) {
        this.searchParams = searchParams
        compositeDisposable += searchSongs.invoke(searchParams)
            .observeOn(rxSchedulers.main)
            .subscribeOn(rxSchedulers.network)
            .doOnSubscribe { _isLoading.value = true }
            .doOnSuccess { result ->
                when (result) {
                    is Resource.Data -> onList(result.result)
                    is Resource.Error -> onError(result.repositoryError)
                }
                _isLoading.value = false
            }
            .subscribe()
    }

    private fun onList(list: List<Song>) {
        _list.value = list.map(this::mapToState)
        _isEmptyView.value = list.isEmpty()
    }

    private fun onError(repositoryError: RepositoryError) {
        appEventBus.onNext(handle(repositoryError))
        _isEmptyView.value = true
    }

    private fun mapToState(song: Song) = SongViewState(
        title = song.title ?: "-",
        subtitle = song.artist,
        smallText = song.releaseYear?.toString() ?: "-",
        imageUrl = song.imageUrl,
        isRightCornerImage = song.imageUrl == null
    )

    private fun handle(repositoryError: RepositoryError) = Alert(
        title = resourceProvider.getString(R.string.ErrorTitle),
        message = repositoryError.message(resourceProvider)
    ).addOkAction { }

    fun itemClicked(position: Int) {

    }

    fun onSource(source: Source?) {
        if (source != null) {
            search(searchParams.copy(source = source))
        } else {
            onList(listOf())
        }
    }

    fun onQuery(query: String) {
        search(searchParams.copy(query = if (query.isEmpty()) Query.All else Query.Text(query)))
    }

    fun onSaveInstance(): Pair<Source, String?> =
        Pair(first = searchParams.source, second = (searchParams.query as? Query.Text)?.text)

    fun onRestoreInstance(pair: Pair<Source?, String?>) {
        search(
            searchParams.copy(
                source = pair.first ?: Source.All,
                query = if (pair.second != null) Query.Text(pair.second!!) else Query.All
            )
        )
        _chipConfigs.value = SourceChipGroup.Configuration(
            list = sources,
            selectedIndex = sources.indexOf(pair.first ?: Source.All)
        )
    }
}
