package com.kamilh.findmysong.repository

import com.kamilh.findmysong.R
import com.kamilh.findmysong.utils.ResourceProvider
import retrofit2.HttpException

sealed class RepositoryError {

    abstract fun message(resourceProvider: ResourceProvider): String

    class ConnectionError(val throwable: Throwable) : RepositoryError() {
        override fun message(resourceProvider: ResourceProvider) = resourceProvider.getString(R.string.RepositoryError_connection)
    }

    object ArgumentError : RepositoryError() {
        override fun message(resourceProvider: ResourceProvider) = resourceProvider.getString(R.string.RepositoryError_http)
    }

    object DiscError : RepositoryError() {
        override fun message(resourceProvider: ResourceProvider) = resourceProvider.getString(R.string.RepositoryError_disc)
    }

    object ParseError : RepositoryError() {
        override fun message(resourceProvider: ResourceProvider) = resourceProvider.getString(R.string.RepositoryError_parse)
    }

    sealed class HttpError : RepositoryError() {

        override fun message(resourceProvider: ResourceProvider) = resourceProvider.getString(R.string.RepositoryError_http)

        object BadRequestException : HttpError()
        object UnauthorizedException : HttpError()
        object ForbiddenException : HttpError()
        object NotFoundException : HttpError()
        object ConflictException : HttpError()
        object InternalServerErrorException : HttpError()
        object ServiceUnavailableException : HttpError()
        class UnexpectedException(val httpException: HttpException) : HttpError()

        companion object {
            fun handle(httpException: HttpException): HttpError = when (httpException.code()) {
                400 -> BadRequestException
                401 -> UnauthorizedException
                403 -> ForbiddenException
                404 -> NotFoundException
                409 -> ConflictException
                500 -> InternalServerErrorException
                503 -> ServiceUnavailableException
                else -> UnexpectedException(httpException)
            }
        }
    }

    companion object {
        fun handle(throwable: Throwable): RepositoryError {
            return when (throwable) {
                is HttpException -> HttpError.handle(throwable)
                is IllegalArgumentException -> ArgumentError
                else -> ConnectionError(throwable)
            }
        }
    }
}
