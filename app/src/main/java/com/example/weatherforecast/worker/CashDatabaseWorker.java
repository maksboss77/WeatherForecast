package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.AppDelegate;
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

public class CashDatabaseWorker extends Worker {

    public CashDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Получаем данные из бд в переменную weatherDao, на данном этапе
        // приложение НЕ чистит данные из бд, он их чистит, только когда получает новые
        MainActivity.weatherDao = ((AppDelegate) getApplicationContext())
                .getWeatherDatabase().getWeatherDao();
        // читаем данные из бд
        MainActivity.weathers = (ArrayList<Weather>) MainActivity.weatherDao.getAll();
        MainActivity.weathersFiveDay = QueryUtils.getFiveDays(MainActivity.weathers);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.weathersFiveDay);

        return Result.success();
    }

}
