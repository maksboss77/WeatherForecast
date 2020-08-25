package com.example.weatherforecast.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract WeatherDao getWeatherDao();

}
