package com.example.weatherapitest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonApi {

    @GET("weather")
    Call<Weather> getWeather(@Query("q") String q, @Query("appid") String appid);

}
