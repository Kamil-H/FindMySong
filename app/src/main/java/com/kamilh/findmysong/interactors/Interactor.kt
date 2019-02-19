package com.kamilh.findmysong.interactors

import io.reactivex.Single

interface Interactor<in I, out O> {
    fun invoke(params: I): O
}

interface SingleInteractor<in I, O> : Interactor<I, Single<O>>
