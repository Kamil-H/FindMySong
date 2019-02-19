package com.kamilh.findmysong.views.main

import androidx.lifecycle.LiveData
import com.kamilh.findmysong.base.BaseViewModel
import com.kamilh.findmysong.data.Alert
import com.kamilh.findmysong.data.AppEvent
import com.kamilh.findmysong.data.Loading
import com.kamilh.findmysong.data.Navigation
import com.kamilh.findmysong.di.AppEventBus
import com.kamilh.findmysong.extensions.plusAssign
import com.kamilh.findmysong.utils.RxSchedulers
import com.kamilh.findmysong.utils.SingleLiveEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    rxSchedulers: RxSchedulers,
    appEventBus: AppEventBus
) : BaseViewModel() {

    private val _alert = SingleLiveEvent<Alert>()
    private val _isLoading = SingleLiveEvent<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val alert: LiveData<Alert> = _alert

    init {
        compositeDisposable += appEventBus
            .observeOn(rxSchedulers.main)
            .doOnNext(this::onAppEvent)
            .subscribe()
    }

    private fun onAppEvent(appEvent: AppEvent) {
        when (appEvent) {
            is Navigation -> TODO()
            is Loading -> _isLoading.value = appEvent.isLoading
            is Alert -> _alert.value = appEvent
        }
    }
}
