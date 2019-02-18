package com.kamilh.findmysong.di

import com.kamilh.findmysong.views.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, MainActivityFragmentsModule::class, ViewModelModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}
