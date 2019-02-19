package com.kamilh.findmysong.repository

interface Mapper<F, T> {
    fun map(from: F): T
}

private class MapperToListMapper<F, T>(val singleMapper: Mapper<F, T>) : Mapper<List<F>, List<T>> {
    override fun map(from: List<F>): List<T> = from.map(singleMapper::map)
}

fun <F, T> Mapper<F, T>.toListMapper(): Mapper<List<F>, List<T>> = MapperToListMapper(this)
