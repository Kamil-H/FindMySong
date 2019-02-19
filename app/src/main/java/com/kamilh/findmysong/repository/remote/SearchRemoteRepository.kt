package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.RepositoryError
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.remote.mapper.SongsResponseToSongs
import com.kamilh.findmysong.repository.toListMapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRemoteRepository @Inject constructor(
    private val iTunesApi: ITunesApi,
    private val searchCache: SearchCache,
    private val songsResponseToSongs: SongsResponseToSongs
) : SearchRepository {

    override fun search(query: String): Single<Resource<List<Song>>> {
        return Single.create { source ->
            val cached = searchCache.get(query)
            if (cached != null) {
                source.onSuccess(Resource.Data(cached))
            }
            iTunesApi.searchSongs(query).doOnSuccess {
                val mapped = songsResponseToSongs.toListMapper().map(it.results)
                searchCache.set(query, mapped)
                source.onSuccess(Resource.Data(mapped))
            }.doOnError {
                source.onSuccess(Resource.Error(RepositoryError.handle(it)))
            }.subscribe()
        }
    }
}
