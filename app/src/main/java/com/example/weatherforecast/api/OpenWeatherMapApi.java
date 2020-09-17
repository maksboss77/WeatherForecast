package com.example.weatherforecast.api;

import com.example.weatherforecast.Message;
import com.example.weatherforecast.data.Weather;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
 * http://api.openweathermap.org/data/2.5/weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
 * q=Novokuznetsk,ru
 * lang=ru
 * units=metric
 * appid=31b762ad9bd0b94b1c2a3cecee08e837
 * */

public interface OpenWeatherMapApi {

    @GET("forecast")
    Call<List<Weather>> getFiveWeathersData(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);

    @GET("weather")
    Call<Weather> getCurrentWeathersData(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);

    @GET("weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837")
    Call<List<JsonObject>> getJsonObject();

    @GET("weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837")
    Call<List<JsonObject>> getTest();

    @GET("messages1.json")
    Call<List<Message>> messages();
}
