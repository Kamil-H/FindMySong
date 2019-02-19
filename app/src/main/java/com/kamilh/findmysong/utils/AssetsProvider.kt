package com.kamilh.findmysong.utils

import android.content.Context
import javax.inject.Inject

class AssetsProvider @Inject constructor(
    private val context: Context
) {

    fun file(filename: String): Result<String> {
        return try {
            val inputStream = context.assets.open(filename)
            Result.Value(inputStream.bufferedReader().use {
                it.readText()
            })
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
