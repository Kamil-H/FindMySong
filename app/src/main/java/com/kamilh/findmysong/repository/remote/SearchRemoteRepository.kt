package com.kamilh.findmysong.repository.remote

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.remote.mapper.SongsResponseToSongs
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRemoteRepository @Inject constructor(
    private val iTunesApi: ITunesApi,
    private val searchCache: SearchCache,
    private val songsResponseToSongs: SongsResponseToSongs
) : SearchRepository {

    private fun fromCache(query: String): Single<Resource<List<Song>>> {
        return searchCache.
    }

    override fun search(query: String): Single<Resource<List<Song>>> {
        return searchCache.get(query)
            .flatMap({ onSuccess ->
                Maybe.just(onSuccess)
            }, {
                Maybe.just(listOf())
            }, {
                Maybe.just(listOf())
            })
            .flatMapSingle {
                if (it.isNotEmpty()) {
                    Single.just(Resource.Data(it))
                } else {
                    iTunesApi.searchSongs(query)
                        .flatMap {
                            Single.just(Resource.Data(it))
                        }
                }
            }
    }
}
