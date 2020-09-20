package com.example.weatherforecast.worker;

import android.content.Context;
import android.util.Log;

import com.example.weatherforecast.App;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.WeatherAdapter;
import com.example.weatherforecast.data.DataWeather;
import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.fivedaysjsonschema.Example;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Response;

import static com.example.weatherforecast.MainActivity.adapter;
import static com.example.weatherforecast.MainActivity.summaryWeathers;
import static com.example.weatherforecast.MainActivity.weathers;

public class WeatherFiveDaysWorker extends Worker {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static String CITY = "Novokuznetsk,ru";
    private static String LANG = "ru";
    private static String UNITS = "metric";
    private static String APP_ID = "31b762ad9bd0b94b1c2a3cecee08e837";

    public WeatherFiveDaysWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            getFiveDaysWeatherApi();
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error getCurrentWeatherApi");
        }
        summaryWeathers = DataWeather.getFiveDays(weathers, DATE_FORMAT);
        MainActivity.adapter = new WeatherAdapter(getApplicationContext(), 0, summaryWeathers);

        return Result.success();
    }

    private void getFiveDaysWeatherApi() throws IOException {

        Response<Example> response = App.getApi().getFiveWeathersData(CITY, LANG, UNITS, APP_ID).execute();
        getInformationFiveDaysWeather(response);

    }

    private void getInformationFiveDaysWeather(Response<Example> response) {

        weathers = new ArrayList<Weather>();

        for (int i = 0; i < response.body().getList().size(); i++) {
            weathers.add(DataWeather.getFiveDaysWeathers(response, i));
        }

        summaryWeathers = DataWeather.getFiveDays(weathers, DATE_FORMAT);
        adapter = new WeatherAdapter(getApplicationContext(), 0, summaryWeathers);

    }



}
