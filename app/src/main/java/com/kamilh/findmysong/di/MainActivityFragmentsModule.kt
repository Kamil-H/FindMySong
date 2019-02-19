package com.kamilh.findmysong.di

import com.kamilh.findmysong.views.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentsModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragment(): SearchFragment

}
