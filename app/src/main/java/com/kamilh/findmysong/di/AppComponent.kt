package com.kamilh.findmysong.di

import com.kamilh.findmysong.FindMySong
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class])
interface AppComponent : AndroidInjector<FindMySong> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: FindMySong): Builder
        fun build(): AppComponent
    }

    override fun inject(app: FindMySong)
}
