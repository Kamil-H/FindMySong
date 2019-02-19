package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.views.search.Query
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
        return when (params.query) {
            is Query.Text -> search(params.query, params.source)
            Query.All -> localSearch.invoke(params.query)
        }
    }

    private fun search(query: Query.Text, source: Source): Single<Resource<List<Song>>> {
        return when (source) {
            Source.Remote -> remoteSearch.invoke(query.text)
            Source.Local -> localSearch.invoke(query)
            Source.All -> Single.zip(localSearch.invoke(query), remoteSearch.invoke(query.text), BiFunction { t1, t2 ->
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
