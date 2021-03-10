package com.remon.doordash.restaurantfinder.viewmodel

import androidx.lifecycle.*
import com.remon.doordash.restaurantfinder.data.ApiResult
import com.remon.doordash.restaurantfinder.data.RestaurantDetail
import com.remon.doordash.restaurantfinder.repository.RestaurantsRepository

open class RestaurantViewModel(val restaurantId: Int, private val repository: RestaurantsRepository) : ViewModel() {
    open class Factory(private val restaurantId: Int, private val repo: RestaurantsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RestaurantViewModel(restaurantId, repo) as T
        }
    }

    private val restaurantDetailRequest: MutableLiveData<Int> = MutableLiveData()
    private val restaurantDetailResult: LiveData<ApiResult<RestaurantDetail>> = Transformations.switchMap(restaurantDetailRequest) {
        repository.getRestaurantDetails(it)
    }

    // Observable to read a list of updated restaurants
    open var restaurantDetail: LiveData<RestaurantDetail?> = Transformations.map(restaurantDetailResult) {
        if (it is ApiResult.SUCCESS) {
            it.data
        } else null
    }

    open var error: LiveData<String?> = Transformations.map(restaurantDetailResult) {
        if (it is ApiResult.FAILURE) {
            it.message
        } else null
    }

    fun fetchDetails() {
        restaurantDetailRequest.value = restaurantId
    }
}