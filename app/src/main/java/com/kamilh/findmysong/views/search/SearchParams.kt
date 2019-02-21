package com.kamilh.findmysong.views.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SearchParams(
    val query: Query = Query.All,
    val source: Source = Source.Local
)

sealed class Query {
    class Text(val text: String): Query()
    object All: Query()
}

@Parcelize
enum class Source : Parcelable {
    Remote, Local, All,
}
