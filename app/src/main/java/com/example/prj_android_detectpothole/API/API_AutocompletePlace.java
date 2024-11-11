package com.example.prj_android_detectpothole.API;

import com.example.prj_android_detectpothole.MODEL.MySearchLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API_AutocompletePlace {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();

    API_AutocompletePlace api_autocomplete= new Retrofit.Builder()
            .baseUrl("https://api.geoapify.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_AutocompletePlace.class);

    @GET("v1/geocode/autocomplete")
    Call<MySearchLocation> getAutocompletePlace(@Query("text") String text,
                                               @Query("apiKey") String apiKey);
}
