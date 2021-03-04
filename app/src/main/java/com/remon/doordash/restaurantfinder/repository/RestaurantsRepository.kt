package com.remon.doordash.restaurantfinder.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.remon.doordash.restaurantfinder.data.StoreFeed
import com.remon.doordash.restaurantfinder.data.StoreFeedResult
import com.remon.doordash.restaurantfinder.services.DoorDashApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantsRepository(private val apiService: DoorDashApiService) {

    private val storeFeed = MutableLiveData<StoreFeedResult<StoreFeed>>()

    fun getRestaurantsAround(lat: Double, lng: Double) : LiveData<StoreFeedResult<StoreFeed>> {

        apiService.defaultStoreFeed(lat, lng).enqueue(object: Callback<StoreFeed> {
            override fun onFailure(call: Call<StoreFeed>, t: Throwable) {
                storeFeed.value = StoreFeedResult.FAILURE(t.toString())
            }

            override fun onResponse(call: Call<StoreFeed>, response: Response<StoreFeed>) {
                if (response.isSuccessful) {
                    storeFeed.value = response.body()?.let { StoreFeedResult.SUCCESS(it) }
                } else {
                    storeFeed.value = response.errorBody()?.string()?.let {
                        StoreFeedResult.FAILURE(it)
                    }
                }
            }
        })

        return storeFeed
    }
}