package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.AppDelegate;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.data.Weather;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AddDataWorker extends Worker {

    public AddDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        MainActivity.weatherDao = ((AppDelegate) getApplicationContext())
                .getWeatherDatabase().getWeatherDao();
        MainActivity.weatherDao.insert(MainActivity.weathers);
        System.out.println((ArrayList<Weather>) MainActivity.weatherDao.getAll());
        return Result.success();
    }
}
