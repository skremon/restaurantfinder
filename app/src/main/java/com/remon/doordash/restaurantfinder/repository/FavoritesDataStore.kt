package com.remon.doordash.restaurantfinder.repository

import android.content.SharedPreferences

class FavoritesDataStore(private val sharedPrefs: SharedPreferences) {

    fun getAllFavorites() : List<Int> {
        return sharedPrefs.all.map {
            it.key.toInt()
        }
    }

    fun setFavorite(restaurantId: Int) {
        sharedPrefs.edit().putBoolean(restaurantId.toString(), true)
            .apply()
    }

    fun removeFavorite(restaurantId: Int) {
        sharedPrefs.edit().remove(restaurantId.toString())
            .apply()
    }

    fun isFavorite(id: Int): Boolean {
        return sharedPrefs.contains(id.toString())
    }

}
