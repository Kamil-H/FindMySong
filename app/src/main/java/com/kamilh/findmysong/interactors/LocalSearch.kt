package com.kamilh.findmysong.interactors

import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.file.SongsRepository
import com.kamilh.findmysong.views.search.Query
import io.reactivex.Single
import javax.inject.Inject

class LocalSearch @Inject constructor(
    private val songsRepository: SongsRepository
) : SingleInteractor<Query, Resource<List<Song>>> {

    override fun invoke(params: Query): Single<Resource<List<Song>>> {
        return when (params) {
            is Query.Text -> songsRepository.songs(params.text)
            Query.All -> songsRepository.songs()
        }
    }
}
