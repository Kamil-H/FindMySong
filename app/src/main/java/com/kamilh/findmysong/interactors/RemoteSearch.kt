package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.remote.SearchRepository
import com.kamilh.findmysong.views.search.Query
import io.reactivex.Single
import javax.inject.Inject

class RemoteSearch @Inject constructor(
    private val searchRepository: SearchRepository
) : SingleInteractor<Query, Resource<List<Song>>> {

    override fun invoke(params: Query): Single<Resource<List<Song>>> {
        return when (params) {
            is Query.Text -> searchRepository.search(params.text)
            Query.All -> Single.create { it.onSuccess(Resource.Data(listOf())) }
        }
    }
}
