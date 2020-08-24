package com.example.weatherforecast;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NowUploadWorker extends Worker {

    public NowUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weather = QueryUtils.extractWeatherNow();

        return Result.success();
    }
}
