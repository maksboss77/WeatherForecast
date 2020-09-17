package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.MainActivity;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FillDatabaseWorker extends Worker {

    public FillDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weatherDao.insert(MainActivity.weathers);
        return Result.success();
    }


}
