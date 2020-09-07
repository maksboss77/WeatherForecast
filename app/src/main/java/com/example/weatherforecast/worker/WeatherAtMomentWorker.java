package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.DataRequestFromServer;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherAtMomentWorker extends Worker {

    public WeatherAtMomentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Запрос на получение текущей погоды

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weather = DataRequestFromServer.getCurrentWeatherFromJSON();

        return Result.success();
    }
}
