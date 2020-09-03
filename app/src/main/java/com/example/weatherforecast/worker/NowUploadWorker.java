package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.QueryUtils;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NowUploadWorker extends Worker {

    public NowUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Запрос на получение текущей погоды

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weather = QueryUtils.extractWeatherNow();

        return Result.success();
    }
}
