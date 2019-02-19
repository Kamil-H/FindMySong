package com.kamilh.findmysong.repository.file

import com.google.gson.Gson
import com.kamilh.findmysong.data.Song
import com.kamilh.findmysong.repository.RepositoryError
import com.kamilh.findmysong.repository.Resource
import com.kamilh.findmysong.repository.file.mapper.FileSongToSong
import com.kamilh.findmysong.repository.file.model.FileSong
import com.kamilh.findmysong.repository.toListMapper
import com.kamilh.findmysong.utils.AssetsProvider
import com.kamilh.findmysong.utils.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongsFileRepository @Inject constructor(
    private val gson: Gson,
    private val assetsProvider: AssetsProvider,
    private val fileSongToSong: FileSongToSong,
    private val songCache: SongCache
) : SongsRepository {

    override fun songs(): Single<Resource<List<Song>>> {
        return Single.create { source ->
            val cached = songCache.get()
            if (cached != null) {
                source.onSuccess(Resource.Data(cached))
            }
            val fileContent = assetsProvider.file("songs-list.json")
            source.onSuccess(
                when (fileContent) {
                    is Result.Value -> {
                        try {
                            val parsed = gson.fromJson(fileContent.value, Array<FileSong>::class.java).toList()
                            val mapped = fileSongToSong.toListMapper().map(parsed)
                            songCache.set(mapped)
                            Resource.Data(mapped)
                        } catch (exception: Exception) {
                            Resource.Error(RepositoryError.ParseError)
                        }
                    }
                    is Result.Error -> Resource.Error(RepositoryError.DiscError)
                }
            )
        }
    }
}
