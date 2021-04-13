package com.remon.doordash.restaurantfinder.services

import android.content.Context
import com.remon.doordash.restaurantfinder.repository.FavoritesDataStore
import com.remon.doordash.restaurantfinder.repository.RestaurantsRepository
import com.remon.doordash.restaurantfinder.viewmodel.FinderViewModel
import com.remon.doordash.restaurantfinder.viewmodel.RestaurantViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private val BASE_URL = "https://api.doordash.com"
    private val FAVORITES_FILE = "favorite_restaurants"

    fun provideDoorDashService() : DoorDashApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(DoorDashApiService::class.java)
    }

    fun provideFinderViewModelFactory(context: Context): FinderViewModel.Factory {
        return FinderViewModel.Factory(
            RestaurantsRepository(
                provideDoorDashService(),
                provideFavoritesDataStore(context)
            )
        )
    }

    private fun provideFavoritesDataStore(context: Context): FavoritesDataStore {
        return FavoritesDataStore(context.getSharedPreferences(FAVORITES_FILE, Context.MODE_PRIVATE))
    }

    fun provideRestaurantViewModelFactory(restaurantId: Int, context: Context): RestaurantViewModel.Factory {
        return RestaurantViewModel.Factory(restaurantId, RestaurantsRepository(provideDoorDashService(), provideFavoritesDataStore(context))
        )
    }
}