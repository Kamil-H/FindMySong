package com.kamilh.findmysong

import com.kamilh.findmysong.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins

class FindMySong : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun applicationInjector() = DaggerAppComponent.builder()
            .application(this)
            .build()
}
