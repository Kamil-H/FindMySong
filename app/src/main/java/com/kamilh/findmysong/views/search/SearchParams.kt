package com.kamilh.findmysong.views.search

data class SearchParams(
    val query: Query = Query.All,
    val source: Source = Source.Local
)

sealed class Query {
    class Text(val text: String): Query()
    object All: Query()
}

sealed class Source {
    object Remote : Source()
    object Local : Source()
    object All : Source()
}
