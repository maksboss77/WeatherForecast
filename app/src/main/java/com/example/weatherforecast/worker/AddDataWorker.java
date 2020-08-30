package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.AppDelegate;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.data.Weather;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AddDataWorker extends Worker {

    public AddDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Заполнение бд данными, если id совпадает, то переписываются значения
    // (например, погода могла на завтрашний день поменяться)
    @NonNull
    @Override
    public Result doWork() {


        MainActivity.weatherDao.insert(MainActivity.weathers);
        System.out.println((ArrayList<Weather>) MainActivity.weatherDao.getAll());
        return Result.success();
    }


}
