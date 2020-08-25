package com.example.weatherforecast;

import android.app.Application;

import com.example.weatherforecast.data.WeatherDatabase;

import androidx.room.Room;

public class AppDelegate extends Application {

    private WeatherDatabase mWeatherDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mWeatherDatabase = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDatabase.class,
                "weather_database")
                .allowMainThreadQueries()
                .build();
    }

    public WeatherDatabase getWeatherDatabase() {
        return mWeatherDatabase;
    }
}
