package com.example.weatherforecast.worker;

import android.content.Context;
import android.util.Log;

import com.example.weatherforecast.App;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.DataRequestFromServer;
import com.example.weatherforecast.currentjsonschema.Example;
import com.example.weatherforecast.currentjsonschema.Main;
import com.example.weatherforecast.data.DataWeather;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherAtMomentWorker extends Worker {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static String CITY = "Novokuznetsk,ru";
    private static String LANG = "ru";
    private static String UNITS = "metric";
    private static String APP_ID = "31b762ad9bd0b94b1c2a3cecee08e837";

    public WeatherAtMomentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            getCurrentWeatherApi();
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error getCurrentWeatherApi");
        }

        return Result.success();
    }

    private void getCurrentWeatherApi() throws IOException {
        Response<Example> response = App.getApi().getCurrentWeather(CITY, LANG, UNITS, APP_ID).execute();
        getInformationCurrentWeather(response);

    }

    private void getInformationCurrentWeather(Response<Example> response) {

        MainActivity.tempCurrentWeather = getCurrentTemp(response);
        MainActivity.descriptionCurrentWeather = getCurrentDescription(response);
        MainActivity.iconCurrentWeather = getCurrentIcon(response);

    }


    private String getCurrentIcon(Response<Example> response) {
        return response.body().getWeather().get(0).getIcon();
    }

    private String getCurrentDescription(Response<Example> response) {
        return response.body().getWeather().get(0).getDescription();
    }

    private int getCurrentTemp(Response<Example> response) {
        return (int) Math.round(response.body().getMain().getTemp());
    }

}
