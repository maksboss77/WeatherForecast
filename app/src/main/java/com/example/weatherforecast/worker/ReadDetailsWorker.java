package com.example.weatherforecast.worker;

import android.content.Context;

import com.example.weatherforecast.DateConversion;
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




    public ReadDetailsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Информация по конкретному дню
    @NonNull
    @Override
    public Result doWork() {

        long startDay = DateConversion.getStartDay(DetailActivity.indexSelectedElement);

        long endDay = DateConversion.getEndDay(startDay);

        DetailActivity.detailsWeathers = (ArrayList<Weather>) MainActivity.weatherDao.oneDayQuery(startDay, endDay);
        DetailActivity.adapter = new DetailsAdapter(getApplicationContext(), 0, DetailActivity.detailsWeathers);

        return Result.success();
    }




}