package com.nookdev.calendartest.model

interface Mapper<in K, out V> {
    suspend fun map(item: K): V
}