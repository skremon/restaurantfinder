package com.remon.doordash.restaurantfinder.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.remon.doordash.restaurantfinder.data.*
import com.remon.doordash.restaurantfinder.services.DoorDashApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Talks to api services to get data and publish them in observable LiveData objects.
 */
class RestaurantsRepository(private val apiService: DoorDashApiService, private val favoritesDataStore: FavoritesDataStore) {

    private val restaurantList = MutableLiveData<ApiResult<List<RestaurantClient>>>()
    private val restaurantDetail = MutableLiveData<ApiResult<RestaurantDetail>>()

    fun getRestaurantsAround(lat: Double, lng: Double) : LiveData<ApiResult<List<RestaurantClient>>> {

        apiService.defaultStoreFeed(lat, lng).enqueue(object: Callback<StoreFeed> {

            override fun onFailure(call: Call<StoreFeed>, t: Throwable) {
                restaurantList.value = ApiResult.FAILURE(t.toString())           }

            override fun onResponse(call: Call<StoreFeed>, response: Response<StoreFeed>) {
                if (response.isSuccessful) {

                    restaurantList.value = response.body()?.let {
                        it.restaurants.map {
                            it.toClient(favoritesDataStore.isFavorite(it.id))
                        }.let {
                            ApiResult.SUCCESS(it)
                        }
                    }
                } else {
                    restaurantList.value = response.errorBody()?.string()?.let {
                        ApiResult.FAILURE(it)
                    }
                }
            }
        })

        return restaurantList
    }

    fun getRestaurantDetails(id: Int): LiveData<ApiResult<RestaurantDetail>> {
        apiService.getRestaurantDetails(id).enqueue(object: Callback<RestaurantDetail> {
            override fun onResponse(call: Call<RestaurantDetail>, response: Response<RestaurantDetail>) {
                if (response.isSuccessful) {
                    restaurantDetail.value = response.body()?.let { ApiResult.SUCCESS(it) }
                } else {
                    restaurantDetail.value = response.errorBody()?.string()?.let {
                        ApiResult.FAILURE(it)
                    }
                }
            }

            override fun onFailure(call: Call<RestaurantDetail>, t: Throwable) {
                restaurantDetail.value = ApiResult.FAILURE(t.toString())
            }
        })

        return restaurantDetail
    }

    fun toggleFavorite(id: Int) {
        var isFavorite = false
        if (favoritesDataStore.isFavorite(id)) {
            favoritesDataStore.removeFavorite(id)
            isFavorite = false
        } else {
            favoritesDataStore.setFavorite(id)
            isFavorite = true
        }

        restaurantList.value = restaurantList.value?.let {
            it.data?.map {
                if (it.id == id) {
                    it.copy(favorite = isFavorite)
                } else {
                    it
                }
            }?.let {
                ApiResult.SUCCESS(it)
            }
        }
    }
}