package com.remon.doordash.restaurantfinder.services

import com.remon.doordash.restaurantfinder.repository.RestaurantsRepository
import com.remon.doordash.restaurantfinder.viewmodel.FinderViewModel
import com.remon.doordash.restaurantfinder.viewmodel.RestaurantViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private val BASE_URL = "https://api.doordash.com"

    fun provideDoorDashService() : DoorDashApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(DoorDashApiService::class.java)
    }

    fun provideFinderViewModelFactory(): FinderViewModel.Factory {
        return FinderViewModel.Factory(RestaurantsRepository(provideDoorDashService()))
    }

    fun provideRestaurantViewModelFactory(restaurantId: Int): RestaurantViewModel.Factory {
        return RestaurantViewModel.Factory(restaurantId, RestaurantsRepository(provideDoorDashService()))
    }
}