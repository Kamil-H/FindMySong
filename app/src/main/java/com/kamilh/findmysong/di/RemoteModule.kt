package com.kamilh.findmysong.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kamilh.findmysong.BuildConfig
import com.kamilh.findmysong.repository.remote.ITunesApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object RemoteModule {

    @Provides @Singleton @JvmStatic
    fun provideITunesApi(okHttpClient: OkHttpClient, gson: Gson): ITunesApi = retrofitCreator(
        "https://itunes.apple.com/search/",
        okHttpClient,
        gson
    ).create(ITunesApi::class.java)

    @Provides @Singleton @JvmStatic
    fun provideGson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

    @Provides @Singleton @JvmStatic
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides @Singleton @JvmStatic
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    private fun retrofitCreator(url: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
