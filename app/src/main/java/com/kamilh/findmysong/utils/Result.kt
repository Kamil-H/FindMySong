package com.kamilh.findmysong.utils

import java.lang.Exception

sealed class Result<out T> {
    class Value<out T>(val value: T) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}
