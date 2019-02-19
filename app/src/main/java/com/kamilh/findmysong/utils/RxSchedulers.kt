package com.kamilh.findmysong.utils

import io.reactivex.Scheduler

data class RxSchedulers(
    val database: Scheduler,
    val disk: Scheduler,
    val network: Scheduler,
    val main: Scheduler
)
