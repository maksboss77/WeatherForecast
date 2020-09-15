package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.AppDelegate;
import com.example.weatherforecast.DateConversion;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.WeatherAdapter;
import com.example.weatherforecast.data.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TakeSavedDataWorker extends Worker {

    private static final int NOT_USE = 0;

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static final long DATE_TRANSITION = 1000L;

    private static final String TODAY = "Сегодня";
    private static final String TOMORROW = "Завтра";

    public TakeSavedDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.weatherDao = ((AppDelegate) getApplicationContext())
                .getWeatherDatabase().getWeatherDao();

        MainActivity.weatherDao.deleteOldRow(Calendar.getInstance().getTimeInMillis() / DATE_TRANSITION);

        MainActivity.weathers = (ArrayList<Weather>) MainActivity.weatherDao.getAll();
        MainActivity.summaryWeathers = getFiveDays(MainActivity.weathers);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, MainActivity.summaryWeathers);

        return Result.success();
    }



    private ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList ) {

        int averageTemp = 0, countTemp = 0, temp = 0, prevIndex = 0;

        long prevDateMilliseconds = 0;
        String prevDate = "", date = "", icon = "";

        ArrayList<Weather> fiveWeather = new ArrayList<>();
        Weather weather;

        for (int i = 0; i < weatherArrayList.size(); i++) {

            weather = weatherArrayList.get(i);

            date = DateConversion.getDateInMilliseconds(weather.getDate(), DATE_FORMAT);
            icon = weather.getIcon();
            temp = weather.getTemp();

            if (prevDate.isEmpty()) {
                prevDate = date;
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

                Weather averageWeather = new Weather(prevDateMilliseconds, t, icon);

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
