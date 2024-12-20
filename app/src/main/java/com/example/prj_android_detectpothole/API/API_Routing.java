package com.example.prj_android_detectpothole.API;

import com.example.prj_android_detectpothole.MODEL.MyRouting;
import com.example.prj_android_detectpothole.MODEL.MyRouting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API_Routing {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();

    API_Routing api_routing= new Retrofit.Builder()
            .baseUrl("https://api.geoapify.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_Routing.class);

    @GET("v1/routing")
    Call<MyRouting> getRouting(@Query("waypoints") String waypoints,
                               @Query("mode") String mode,
                               @Query("apiKey") String apiKey);
    @GET("v1/routing")
    Call<MyRouting> getRoutingAvoidPotholes(@Query("waypoints") String waypoints,
                                            @Query("mode") String mode,
                                            @Query("avoid") String advoid,
                                            @Query("apiKey") String apiKey);
}
