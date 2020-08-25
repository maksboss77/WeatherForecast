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
    private int prevDateMilliseconds = 0;

    private ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList ) {

        ArrayList<Weather> fiveWeather = new ArrayList<>();

        Weather weather;

        String date = "";
        String icon;
        int temp;

        int prevIndex = 0;

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

                int t = (int)Math.round(averageTemp/(countTemp*1.0));

                int maxDifferent = 100;

                for (int j = prevIndex; j < i; j++) {
                    Weather searchIcon = weatherArrayList.get(j);

                    if (Math.abs(searchIcon.getTemp() - t) < maxDifferent) {
                        maxDifferent = Math.abs(searchIcon.getTemp() - t);
                        icon = searchIcon.getIcon();
                    }

                    System.out.println("[" + i + "," + j + "] = " + "icon:" + searchIcon.getIcon() + ", temp: " + searchIcon.getTemp());
                }

                Weather averageWeather = new Weather(
                        prevDateMilliseconds,
                        t,
                        0,
                        0,
                        0,
                        0,
                        "",
                        icon);

                fiveWeather.add(averageWeather);
                averageTemp = weather.getTemp();
                countTemp = 1;
                prevDate = date;
                prevIndex = i;
            } else {
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            }



        }

        return fiveWeather;

    }

    private String getDateString(int timeInMilliseconds) {

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
