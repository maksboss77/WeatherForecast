package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.DateConversion;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.DataRequestFromServer;
import com.example.weatherforecast.WeatherAdapter;
import com.example.weatherforecast.data.DataWeather;
import com.example.weatherforecast.data.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WeatherFiveDaysWorker extends Worker {

    private static final int NOT_USE = 0;

    private static final String DATE_FORMAT = "dd.MM.yyyy";



    public WeatherFiveDaysWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weathers = DataWeather.getFiveWeathersFromJSON();
        MainActivity.summaryWeathers = DataWeather.getFiveDays(MainActivity.weathers, DATE_FORMAT);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.summaryWeathers);

        return Result.success();
    }

}
