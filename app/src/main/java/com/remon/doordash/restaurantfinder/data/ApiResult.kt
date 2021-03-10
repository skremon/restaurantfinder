package com.remon.doordash.restaurantfinder.data

/**
 * Used to hold and pass data from a api response along with error states.
 */
sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class SUCCESS<T>(data: T) : ApiResult<T>(data)
    class FAILURE<T>(error: String) : ApiResult<T>(null, error)
    // There could be a LOADING state here if needed
}
