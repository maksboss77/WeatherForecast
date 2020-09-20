package com.example.weatherforecast.api;

import com.example.weatherforecast.currentjsonschema.Example;
import com.example.weatherforecast.currentjsonschema.Wind;
import com.example.weatherforecast.data.Weather;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
 * http://api.openweathermap.org/data/2.5/weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
 * q=Novokuznetsk,ru
 * lang=ru
 * units=metric
 * appid=31b762ad9bd0b94b1c2a3cecee08e837
 */

public interface OpenWeatherMapApi {

    @GET("forecast")
    Call<com.example.weatherforecast.fivedaysjsonschema.Example> getFiveWeathersData(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);


    @GET("weather")
    Call<Example> getCurrentWeather(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);


    @GET("weather")
    Call<JsonObject> getJsonObject(@Query("q") String city,
                                   @Query("lang") String lang,
                                   @Query("units") String units,
                                   @Query("appid") String appId);



}
