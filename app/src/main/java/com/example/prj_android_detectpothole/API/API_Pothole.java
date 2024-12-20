package com.example.prj_android_detectpothole.API;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.example.prj_android_detectpothole.OBJECT.MyMarker;
import com.example.prj_android_detectpothole.OBJECT.MyUserToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API_Pothole {
    String token = "Bearer "+MyUserToken.token;

    Interceptor interceptor = new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("token",token);
            return chain.proceed(builder.build());
        }
    };
    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);

    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();

    API_Pothole api_pothole= new Retrofit.Builder()
            .baseUrl("https://api-detect-pothole-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okBuilder.build())
            .build()
            .create(API_Pothole.class);

    @POST("v1/pothole/create")
    Call<MyMarker> post_pothole(@Body MyMarker marker);

    @GET("v1/pothole")
    Call<List<MyMarker>> get_all_pothole();

    @DELETE("v1/pothole/delete/{id}")
    Call<Void> delete_pothole(@Path("id") String id);

    @PUT("v1/pothole/update/{id}")
    Call<MyMarker> update_pothole(@Path("id") String id,@Body MyMarker marker);
}
