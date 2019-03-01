package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.views.search.SearchParams
import com.kamilh.findmysong.views.search.Source
import io.reactivex.Single
import javax.inject.Inject

class SearchSongs @Inject constructor(
    private val localSearch: LocalSearch,
    private val databaseSearch: DatabaseSearch,
    private val remoteSearch: RemoteSearch
) : SingleInteractor<SearchParams, Resource<List<Song>>> {

    override fun invoke(params: SearchParams): Single<Resource<List<Song>>> {
        val list = params.sources.map { source ->
            when (source) {
                Source.Remote -> remoteSearch.invoke(params = params.query)
                Source.Local -> localSearch.invoke(params = params.query)
                Source.Database -> databaseSearch.invoke(params = params.query)
                Source.None -> Single.just(Resource.Data(listOf<Song>()))
            }
        }
        return Single.merge(list).toList().map { resources ->
            val error = resources.firstOrNull { it is Resource.Error }
            if (error != null) {
                 return@map error
            }
            val successes = resources.map { it as Resource.Data }.flatMap { it.result }
            Resource.Data(successes)
        }
    }
}
