package com.kamilh.findmysong.di

import androidx.appcompat.app.AppCompatActivity
import com.kamilh.findmysong.data.AppEvent
import com.kamilh.findmysong.views.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

typealias AppEventBus = PublishSubject<AppEvent>

@Module
object MainActivityModule {

    @Provides @JvmStatic @ActivityScope
    fun provideActivity(mainActivity: MainActivity): AppCompatActivity = mainActivity

    @Provides @JvmStatic @ActivityScope
    fun provideAppEventBus() = AppEventBus.create<AppEvent>()
}
