package com.example.weatherforecast;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("Я вошел в воркер");

        MainActivity.weathers = QueryUtils.extractWeathers();
        MainActivity.weathersFiveDay = getFiveDays(MainActivity.weathers);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.weathersFiveDay);

        return Result.success();
    }

    // Average temperature for the day
    private int averageTemp;

    // Count temperature for the day
    private int countTemp;

    // Previous day
    private String prevDate = "";
    private long prevDateMilliseconds = 0;

    private ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList ) {

        ArrayList<Weather> fiveWeather = new ArrayList<>();

        Weather weather;

        String date = "";
        String icon;
        int temp;

        for (int i = 0; i < weatherArrayList.size(); i++) {

            weather = weatherArrayList.get(i);

            date = getDateString(weather.getDate());
            icon = weather.getIcon();
            temp = weather.getTemp();

            if (prevDate.isEmpty()) {
                prevDate = date;
                prevDateMilliseconds = weather.getDate();
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            } else if (!date.equals(prevDate)) {
                //TODO: Return average icon


                Weather averageWeather = new Weather(prevDateMilliseconds, averageTemp/countTemp,
                        0, 0, 0, 0, "", icon);
                fiveWeather.add(averageWeather);
                averageTemp = 0;
                countTemp = 0;
                prevDate = date;
            } else {
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            }



        }

        return fiveWeather;

    }

    private String getDateString(long timeInMilliseconds) {

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds * 1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Сегодня";
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Завтра";

        return simpleDateFormat.format(calendar.getTime());
    }
}
