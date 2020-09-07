package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.DateConversion;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.DataRequestFromServer;
import com.example.weatherforecast.WeatherAdapter;
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

    // Получение погоды на 5 дней
    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weathers = DataRequestFromServer.getFiveWeathersFromJSON();
        MainActivity.summaryWeathers = getFiveDays(MainActivity.weathers);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.summaryWeathers);

        return Result.success();
    }

    // Средняя температура за денб
    private int averageTemp;

    // Количество температур в одном дне
    private int countTemp;

    // Предыдущая дата
    private String prevDate = "";
    private long prevDateMilliseconds = 0;

    private ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList ) {

        ArrayList<Weather> fiveWeather = new ArrayList<>();

        Weather weather;

        String date = "";
        String icon;
        int temp;

        int prevIndex = 0;

        for (int i = 0; i < weatherArrayList.size(); i++) {

            weather = weatherArrayList.get(i);

            date = DateConversion.getDateSpecificFormat(weather.getDate(), DATE_FORMAT);
            icon = weather.getIcon();
            temp = weather.getTemp();

            if (prevDate.isEmpty()) {
                prevDate = date;
                prevDateMilliseconds = weather.getDate();
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            } else if (!date.equals(prevDate)) {

                int t = (int) Math.round(averageTemp/(countTemp*1.0));

                int maxDifferent = 100;

                for (int j = prevIndex; j < i; j++) {
                    Weather searchIcon = weatherArrayList.get(j);

                    if (Math.abs(searchIcon.getTemp() - t) < maxDifferent) {
                        maxDifferent = Math.abs(searchIcon.getTemp() - t);
                        icon = searchIcon.getIcon();
                    }

                }

                Weather averageWeather = new Weather(
                        prevDateMilliseconds,
                        t,
                        NOT_USE,
                        NOT_USE,
                        NOT_USE,
                        NOT_USE,
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
}
