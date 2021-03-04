package com.remon.doordash.restaurantfinder.viewmodel

import androidx.lifecycle.*
import com.remon.doordash.restaurantfinder.data.Restaurant
import com.remon.doordash.restaurantfinder.data.StoreFeed
import com.remon.doordash.restaurantfinder.data.StoreFeedResult
import com.remon.doordash.restaurantfinder.repository.RestaurantsRepository

open class FinderViewModel(private val repository: RestaurantsRepository) : ViewModel() {

    class Factory(private val repo: RestaurantsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FinderViewModel(repo) as T
        }
    }

    private val storeFeedRequest: MutableLiveData<StoreFeed.Request> = MutableLiveData()
    private val storeFeedResult: LiveData<StoreFeedResult<StoreFeed>> = Transformations.switchMap(storeFeedRequest) {
        repository.getRestaurantsAround(it.lat, it.lng)
    }

    // Observable to read a list of updated restaurants
    open var restaurants: LiveData<List<Restaurant>?> = Transformations.map(storeFeedResult) {
        if (it is StoreFeedResult.SUCCESS) {
            it.data?.restaurants
        } else null
    }

    open var error: LiveData<String?> = Transformations.map(storeFeedResult) {
        if (it is StoreFeedResult.FAILURE) {
            it.message
        } else null
    }

    // Call to trigger a request to get a list of restaurants around {lat, lng}
    open fun fetchRestaurantsAroundLocation(lat: Double, lng: Double) {
        storeFeedRequest.value = StoreFeed.Request(lat, lng, 0)
    }
}