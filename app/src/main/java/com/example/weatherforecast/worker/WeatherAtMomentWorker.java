package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.DataRequestFromServer;
import com.example.weatherforecast.data.DataWeather;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherAtMomentWorker extends Worker {

    public WeatherAtMomentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weather = DataWeather.getCurrentWeatherFromJSON();

        return Result.success();
    }
}
