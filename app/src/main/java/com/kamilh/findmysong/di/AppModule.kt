package com.kamilh.findmysong.di

import android.content.Context
import com.kamilh.findmysong.FindMySong
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RemoteModule::class])
object AppModule {

    @Provides @Singleton @JvmStatic
    fun provideContext(findMySong: FindMySong): Context = findMySong
}
