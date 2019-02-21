package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.views.search.SearchParams
import com.kamilh.findmysong.views.search.Source
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SearchSongs @Inject constructor(
    private val localSearch: LocalSearch,
    private val remoteSearch: RemoteSearch
) : SingleInteractor<SearchParams, Resource<List<Song>>> {

    override fun invoke(params: SearchParams): Single<Resource<List<Song>>> {
        return when (params.source) {
            Source.Remote -> remoteSearch.invoke(params.query)
            Source.Local -> localSearch.invoke(params.query)
            Source.All -> Single.zip(localSearch.invoke(params.query), remoteSearch.invoke(params.query), BiFunction { t1, t2 ->
                when (t1) {
                    is Resource.Data -> when (t2) {
                        is Resource.Data -> Resource.Data(t2.result.union(t1.result).toList())
                        is Resource.Error -> t2
                    }
                    is Resource.Error -> t1
                }
            })
        }
    }
}
