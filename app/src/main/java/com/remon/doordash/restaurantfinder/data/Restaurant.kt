package com.remon.doordash.restaurantfinder.data

import com.google.gson.annotations.SerializedName

data class Restaurant(val id: Int,
                      val name: String,
                      val description: String,
                      @SerializedName("cover_img_url") val coverImageUrl: String)
