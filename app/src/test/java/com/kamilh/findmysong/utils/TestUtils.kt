package com.kamilh.findmysong.utils

import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}
fun <T> uninitialized(): T = null as T
