package com.remon.doordash.restaurantfinder.data

import com.google.gson.annotations.SerializedName

data class StoreFeed(@SerializedName("num_results") val numResults: Int,
                     @SerializedName("next_offset") val nextOffset: Int,
                     @SerializedName("stores") val restaurants: List<Restaurant>) {

    data class Request(val lat: Double, val lng: Double, val offset: Int)
}