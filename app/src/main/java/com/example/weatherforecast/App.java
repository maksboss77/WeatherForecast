package com.example.weatherforecast;

import android.app.Application;

import com.example.weatherforecast.api.OpenWeatherMapApi;
import com.example.weatherforecast.data.WeatherDatabase;

import androidx.room.Room;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String NAME_DATABASE = "weather_database";

    private WeatherDatabase mWeatherDatabase;

    private Retrofit retrofit;
    private static OpenWeatherMapApi openWeatherMapApi;

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";


    @Override
    public void onCreate() {
        super.onCreate();

        mWeatherDatabase = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDatabase.class,
                NAME_DATABASE)
                .build();

        /**Основной*/
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);

    }

    public WeatherDatabase getWeatherDatabase() {
        return mWeatherDatabase;
    }

    public static OpenWeatherMapApi getApi() {
        return openWeatherMapApi;
    }

}
