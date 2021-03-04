package com.remon.doordash.restaurantfinder.data

sealed class StoreFeedResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class SUCCESS<T>(data: T) : StoreFeedResult<T>(data)
    class FAILURE<T>(error: String) : StoreFeedResult<T>(null, error)
}
