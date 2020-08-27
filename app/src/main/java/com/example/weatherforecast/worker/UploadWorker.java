package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.QueryUtils;
import com.example.weatherforecast.WeatherAdapter;
import com.example.weatherforecast.data.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weathers = QueryUtils.extractWeathers();
        MainActivity.weathersFiveDay = QueryUtils.getFiveDays(MainActivity.weathers);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.weathersFiveDay);

        return Result.success();
    }

}
