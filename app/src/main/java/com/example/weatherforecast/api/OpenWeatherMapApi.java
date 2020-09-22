package com.example.weatherforecast.api;

import com.example.weatherforecast.currentjsonschema.CurrentWeatherJSON;
import com.example.weatherforecast.fivedaysjsonschema.WeatherForFiveDaysJSON;

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
    Call<WeatherForFiveDaysJSON> getFiveWeathersData(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);


    @GET("weather")
    Call<CurrentWeatherJSON> getCurrentWeather(
            @Query("q") String city,
            @Query("lang") String lang,
            @Query("units") String units,
            @Query("appid") String appId);

}
