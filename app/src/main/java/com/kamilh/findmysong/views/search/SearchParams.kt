package com.kamilh.findmysong.views.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SearchParams(
    val query: Query = Query.All,
    val sources: Set<Source>
)

sealed class Query {
    class Text(val text: String): Query()
    object All: Query()
}

@Parcelize
enum class Source : Parcelable {
    Remote, Local, Database, None
}
