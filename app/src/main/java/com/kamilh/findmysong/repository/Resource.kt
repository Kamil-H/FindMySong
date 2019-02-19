package com.kamilh.findmysong.repository

sealed class Resource<out T> {
    class Data<out T>(val result: T): Resource<T>()
    class Error(val repositoryError: RepositoryError): Resource<Nothing>()
}
