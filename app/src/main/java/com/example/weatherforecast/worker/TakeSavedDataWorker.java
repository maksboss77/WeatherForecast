package com.example.weatherforecast.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weatherforecast.App;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.WeatherAdapter;
import com.example.weatherforecast.data.DataWeather;
import com.example.weatherforecast.data.Weather;

import java.util.ArrayList;
import java.util.Calendar;

public class TakeSavedDataWorker extends Worker {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static final long DATE_TRANSITION = 1000L;

    public TakeSavedDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weatherDao = ((App) getApplicationContext())
                .getWeatherDatabase().getWeatherDao();

        MainActivity.weatherDao.deleteOldRow(Calendar.getInstance().getTimeInMillis() / DATE_TRANSITION);

        MainActivity.weathers = (ArrayList<Weather>) MainActivity.weatherDao.getAll();
        MainActivity.summaryWeathers = DataWeather.getFiveDays(MainActivity.weathers, DATE_FORMAT);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.summaryWeathers);

        return Result.success();
    }

}
