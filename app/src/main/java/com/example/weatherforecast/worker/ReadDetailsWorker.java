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
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, index);
        calendar.add(Calendar.HOUR_OF_DAY, +1);
        System.out.println("START DAY: " + (calendar.getTimeInMillis()/1000));
        startDay = calendar.getTimeInMillis()/1000;
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        System.out.println("END DAY: " + (calendar.getTimeInMillis()/1000));
        endDay = calendar.getTimeInMillis()/1000;
    }


}