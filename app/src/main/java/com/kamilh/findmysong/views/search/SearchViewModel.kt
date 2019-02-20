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
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchSongs: SearchSongs,
    private val rxSchedulers: RxSchedulers,
    private val resourceProvider: ResourceProvider,
    private val appEventBus: AppEventBus
) : BaseViewModel() {

    private val _list = MutableLiveData<List<SongViewState>>()
    private val _title = MutableLiveData<String>()
    private val _isLoading = SingleLiveEvent<Boolean>()
    private val _isEmptyView = MutableLiveData<Boolean>()
    private val _chipConfigs = MutableLiveData<SourceChipGroup.Configuration>()

    val list: LiveData<List<SongViewState>> = _list
    val title: LiveData<String> = _title
    val isLoading: LiveData<Boolean> = _isLoading
    val isEmptyView: LiveData<Boolean> = _isEmptyView
    val chipConfigs: LiveData<SourceChipGroup.Configuration> = _chipConfigs

    private val sources = listOf(Source.Remote, Source.Local, Source.All)
    private var searchParams = SearchParams(query = Query.All, source = Source.All)
    private var lastTextEvent: TextEvent? = null

    init {
        updateSource(Source.All)
    }

    private fun updateSource(source: Source) {
        _chipConfigs.value = SourceChipGroup.Configuration(
            list = sources,
            selectedIndex = sources.indexOf(source)
        )
    }

    private fun search(searchParams: SearchParams) {
        this.searchParams = searchParams
        updateTitle(searchParams.query)
        compositeDisposable += searchSongs.invoke(searchParams)
            .observeOn(rxSchedulers.main)
            .subscribeOn(rxSchedulers.network)
            .doOnSubscribe { _isLoading.value = true }
            .subscribe(this::onResource)
    }

    private fun onResource(resource: Resource<List<Song>>) {
        when (resource) {
            is Resource.Data -> onList(resource.result)
            is Resource.Error -> onError(resource.repositoryError)
        }
        _isLoading.value = false
    }

    private fun updateTitle(query: Query) {
        _title.value = if (query is Query.Text) query.text else resourceProvider.getString(R.string.app_name)
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

    private fun handle(repositoryError: RepositoryError): Alert {
        val alert = Alert(
            title = resourceProvider.getString(R.string.ErrorTitle),
            message = repositoryError.message(resourceProvider)
        )

        if (searchParams.source == Source.All) {
            return when (repositoryError) {
                RepositoryError.DiscError, RepositoryError.ParseError -> alert.copy(
                    message = "${alert.message}. ${resourceProvider.getString(R.string.SearchFragment_retry_with_internet)}",
                    positiveButton = Alert.Action(resourceProvider.getString(R.string.yes)) { updateSource(Source.Remote) },
                    negativeButton = Alert.Action(resourceProvider.getString(R.string.no)) { }
                )
                else -> alert.copy(
                    message = "${alert.message}. ${resourceProvider.getString(R.string.SearchFragment_retry_with_local)}",
                    positiveButton = Alert.Action(resourceProvider.getString(R.string.yes)) { updateSource(Source.Local) },
                    negativeButton = Alert.Action(resourceProvider.getString(R.string.no)) { }
                )
            }
        }

        return alert.addOkAction { }
    }

    fun itemClicked(position: Int) {

    }

    fun onSource(source: Source) {
        search(searchParams.copy(source = source))
    }

    fun queryObservable(observable: Observable<TextEvent>) {
        compositeDisposable += observable
            .observeOn(rxSchedulers.main)
            .filter { !((lastTextEvent is TextEvent.Closed || lastTextEvent is TextEvent.Opened) && it is TextEvent.Changed && it.text.isEmpty()) }
            .doOnNext { lastTextEvent = it }
            .subscribe {
                search(searchParams.copy(
                    query = when (it) {
                        TextEvent.Opened -> searchParams.query
                        is TextEvent.Changed -> if (it.text.isEmpty()) Query.All else Query.Text(it.text)
                        is TextEvent.Closed -> if (it.text.isEmpty()) Query.All else Query.Text(it.text)
                    }
                ))
            }
    }

    fun onSaveInstance(): Pair<Source, String?> =
        Pair(first = searchParams.source, second = (searchParams.query as? Query.Text)?.text)

    fun onRestoreInstance(pair: Pair<Source?, String?>) {
        search(
            searchParams.copy(
                source = pair.first ?: Source.All,
                query = pair.second?.run { Query.Text(this) } ?: Query.All
            )
        )
        updateSource(pair.first ?: Source.All)
    }
}
