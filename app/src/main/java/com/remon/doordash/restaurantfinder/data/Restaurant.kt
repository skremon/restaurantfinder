package com.remon.doordash.restaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Restaurant(@SerializedName("id")val id: Int,
                      @SerializedName("name") val name: String,
                      @SerializedName("description") val description: String,
                      @SerializedName("cover_img_url") val coverImageUrl: String) {

    fun toClient(favorite: Boolean): RestaurantClient {
        return RestaurantClient(id, name, description, coverImageUrl, favorite)
    }
}

data class RestaurantClient(
    val id: Int,
    val name: String,
    val description: String,
    val coverImageUrl: String,
    val favorite: Boolean
)