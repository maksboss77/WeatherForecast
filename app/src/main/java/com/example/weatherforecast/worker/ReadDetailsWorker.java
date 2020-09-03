package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.DetailActivity;
import com.example.weatherforecast.DetailsAdapter;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.data.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReadDetailsWorker extends Worker {

    private static final long DATE_TRANSITION = 1000L;

    private static final int SET_TIME = 0;
    private static final int ADD_HOUR_OF_DAY = 1;

    long startDay;
    long endDay;

    public ReadDetailsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Информация по конкретному дню
    @NonNull
    @Override
    public Result doWork() {

        getDate(DetailActivity.index);


        DetailActivity.detailsWeathers = (ArrayList<Weather>) MainActivity.weatherDao.oneDayQuery(startDay, endDay);
        DetailActivity.adapter = new DetailsAdapter(getApplicationContext(), 0, DetailActivity.detailsWeathers);

        return Result.success();
    }

    private void getDate(int index) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, SET_TIME);
        calendar.set(Calendar.MINUTE, SET_TIME);
        calendar.set(Calendar.SECOND, SET_TIME);
        calendar.set(Calendar.MILLISECOND, SET_TIME);

        calendar.add(Calendar.DAY_OF_MONTH, index);
        calendar.add(Calendar.HOUR_OF_DAY, ADD_HOUR_OF_DAY);
        startDay = calendar.getTimeInMillis()/DATE_TRANSITION;

        calendar.add(Calendar.DAY_OF_MONTH, ADD_HOUR_OF_DAY);
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        endDay = calendar.getTimeInMillis()/DATE_TRANSITION;
    }


}