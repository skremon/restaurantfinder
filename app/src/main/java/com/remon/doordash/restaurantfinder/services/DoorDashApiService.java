package com.remon.doordash.restaurantfinder.services;

import com.remon.doordash.restaurantfinder.data.StoreFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DoorDashApiService {

    /**
        /v1/store_feed/?lat=<LAT>&lng=<LNG>&offset=0&limit=50
     */
    @GET("v1/store_feed?offset=0&limit=50")
    Call<StoreFeed> defaultStoreFeed(@Query("lat") double lat,
                                     @Query("lng") double lng);

    @GET("v1/store_feed?&limit=50")
    Call<StoreFeed> storeFeedByPageOffset(@Query("lat") double lat,
                                          @Query("lng") double lng,
                                          @Query("offset") int offset);
}
