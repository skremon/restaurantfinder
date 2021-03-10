package com.remon.doordash.restaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Restaurant(@SerializedName("id")val id: Int,
                      @SerializedName("name") val name: String,
                      @SerializedName("description") val description: String,
                      @SerializedName("cover_img_url") val coverImageUrl: String)
