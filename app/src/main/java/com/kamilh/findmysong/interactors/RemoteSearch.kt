package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.remote.SearchRepository
import io.reactivex.Single
import javax.inject.Inject

class RemoteSearch @Inject constructor(
    private val searchRepository: SearchRepository
) : SingleInteractor<String, Resource<List<Song>>> {

    override fun invoke(params: String): Single<Resource<List<Song>>> {
        return searchRepository.search(params)
    }
}
