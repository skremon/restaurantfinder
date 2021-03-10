package com.remon.doordash.restaurantfinder.data

import com.google.gson.annotations.SerializedName

/**
 * Restaurant model representation from the v2 api.
 * Since the many properties look different than 'Restaurant' this object is added when
 * using v2 apis.
 */
data class RestaurantDetail(@SerializedName("id") val id: Int,
                            @SerializedName("name") val name: String,
                            @SerializedName("average_rating") val averageRating: Float,
                            @SerializedName("cover_img_url") val coverImageUrl: String,
                            @SerializedName("description") val description: String,
                            @SerializedName("status") val status: String)
