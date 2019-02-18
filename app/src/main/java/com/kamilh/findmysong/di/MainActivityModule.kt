package com.kamilh.findmysong.di

import androidx.appcompat.app.AppCompatActivity
import com.kamilh.findmysong.views.MainActivity
import dagger.Module
import dagger.Provides

@Module
object MainActivityModule {

    @Provides @JvmStatic @ActivityScope
    fun provideActivity(mainActivity: MainActivity): AppCompatActivity = mainActivity
}
