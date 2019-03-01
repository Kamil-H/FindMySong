package com.kamilh.findmysong.di

import android.content.Context
import com.kamilh.findmysong.FindMySong
import com.kamilh.findmysong.repository.database.Songs2Repository
import com.kamilh.findmysong.repository.database.SongsDatabaseRepository
import com.kamilh.findmysong.repository.file.SongsFileRepository
import com.kamilh.findmysong.repository.file.SongsRepository
import com.kamilh.findmysong.repository.remote.SearchRemoteRepository
import com.kamilh.findmysong.repository.remote.SearchRepository
import com.kamilh.findmysong.utils.RxSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = [RemoteModule::class])
object AppModule {

    @Provides @Singleton @JvmStatic
    fun provideContext(findMySong: FindMySong): Context = findMySong

    @Provides @Singleton @JvmStatic
    fun provideRxSchedulers() = RxSchedulers(
        database = Schedulers.single(),
        disk = Schedulers.io(),
        network = Schedulers.io(),
        main = AndroidSchedulers.mainThread()
    )

    @Provides @Singleton @JvmStatic
    fun bindSongsRepository(songsFileRepository: SongsFileRepository): SongsRepository = songsFileRepository

    @Provides @Singleton @JvmStatic
    fun bindSongsRepository(songsFileRepository: SongsDatabaseRepository): Songs2Repository = songsFileRepository

    @Provides @Singleton @JvmStatic
    fun bindSearchRepository(searchRemoteRepository: SearchRemoteRepository): SearchRepository = searchRemoteRepository
}
