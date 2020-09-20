package com.example.weatherforecast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.currentjsonschema.Example;
import com.example.weatherforecast.data.DataWeather;
import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.data.WeatherDao;
import com.example.weatherforecast.worker.FillDatabaseWorker;
import com.example.weatherforecast.worker.WeatherAtMomentWorker;
import com.example.weatherforecast.worker.WeatherFiveDaysWorker;
import com.example.weatherforecast.worker.TakeSavedDataWorker;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Имя класса
    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String URL_ICON_BEGIN = "http://openweathermap.org/img/wn/";
    private static final String URL_ICON_END = "@2x.png";

    // Имя нажатой позиции
    private static final String KEY = "index";

    // Общая переменная со всей информацией о погоде за 5 дней
    public static ArrayList<Weather> weathers;

    // Общая переменная с КРАТКОЙ информацией о погоде за 5 дней
    public static ArrayList<Weather> summaryWeathers;

    // Публичная переменная ListView
    public ListView weatherListView;

    // Adapter для отрисовки 5 дней
    public static WeatherAdapter adapter;

    // Погода сейчас
    public static Weather weather;

    //
    public static WeatherDao weatherDao;

    private static String CITY = "Novokuznetsk,ru";
    private static String LANG = "ru";
    private static String UNITS = "metric";
    private static String APP_ID = "31b762ad9bd0b94b1c2a3cecee08e837";


    /**
     * Текущая погода
     * http://api.openweathermap.org/data/2.5/weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
     * <p>
     * Сегодня и на 4 последующих дня
     * http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
     * <p>
     * URL для получения иконки
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Поиск в макете ListView
        weatherListView = (ListView) findViewById(R.id.list);


        getFiveDaysWeatherApi();
        getCurrentWeatherApi();
        startWorker();

        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Activity activity = MainActivity.this;

                Bundle bundle = null;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (view != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, activity.getString(R.string.anim));
                        bundle = options.toBundle();
                    }
                }

                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(KEY, position);
                if (bundle == null) {
                    activity.startActivity(intent);
                } else {
                    activity.startActivity(intent, bundle);
                }

            }
        });


    }

    private void getFiveDaysWeatherApi() {
        Call<com.example.weatherforecast.fivedaysjsonschema.Example> weathersData = App.getApi().getFiveWeathersData(CITY, LANG, UNITS, APP_ID);
        weathersData.enqueue(new Callback<com.example.weatherforecast.fivedaysjsonschema.Example>() {
            @Override
            public void onResponse(Call<com.example.weatherforecast.fivedaysjsonschema.Example> call, Response<com.example.weatherforecast.fivedaysjsonschema.Example> response) {
                getInformationFiveDaysWeather(response);
                System.out.println("Response body five days: " + response.body());
                Log.e(LOG_TAG, "Погода на 5 дней момент получена");
            }

            @Override
            public void onFailure(Call<com.example.weatherforecast.fivedaysjsonschema.Example> call, Throwable t) {
                System.out.println("Failure five days: " + t);
            }
        });


    }

    private void getCurrentWeatherApi() {

        Call<Example> weatherExample = App.getApi().getCurrentWeather(CITY, LANG, UNITS, APP_ID);
        weatherExample.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                getInformationCurrentWeather(response);
                System.out.println("Response body current: " + response.body());
                Log.e(LOG_TAG, "Погода на текущий момент получена");
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                System.out.println("Failure current: " + t);
            }
        });

    }


    private void getInformationFiveDaysWeather(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response) {

        String DATE_FORMAT = "dd.MM.yyyy";

        for (int i = 0; i < response.body().getList().size(); i++) {
            weathers.add(DataWeather.getFiveDaysWeathers(response, i));
        }

        summaryWeathers = DataWeather.getFiveDays(weathers, DATE_FORMAT);
        adapter = new WeatherAdapter(this, 0, summaryWeathers);

    }



    private void getInformationCurrentWeather(Response<Example> response) {
        int temp = getCurrentTemp(response);
        String description = getCurrentDescription(response);
        String icon = getCurrentIcon(response);
        getViewCurrentWeather(temp, description, icon);
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

    private void startWorker() {

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.INVISIBLE);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        OneTimeWorkRequest takeSavedData = new OneTimeWorkRequest
                .Builder(TakeSavedDataWorker.class)
                .build();

        OneTimeWorkRequest weatherAtMoment = new OneTimeWorkRequest
                .Builder(WeatherAtMomentWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest weatherFiveDays = new OneTimeWorkRequest
                .Builder(WeatherFiveDaysWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest fillDatabase = new OneTimeWorkRequest
                .Builder(FillDatabaseWorker.class)
                .setConstraints(constraints)
                .build();

        /** Запускаем Worker в фоновом потоке:
         *  1. Читаем данные из Бд и отображаем ее
         *  2. Делаем запрос на получение текущей погоды
         *  3. Делаем запрос на получение погоды на 5 дней
         *  4. Обновляем данные в БД.
         *  Примечание: каждый пункт выполяется, если выполнены прерыдущие действия цепочки. */

        WorkManager.getInstance(this)
                .beginWith(takeSavedData)
//                .then(weatherAtMoment)
//                .then(weatherFiveDays)
                .then(fillDatabase)
                .enqueue();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(takeSavedData.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            linearLayout.setVisibility(linearLayout.VISIBLE);

                            weatherListView.setAdapter(adapter);
                            Log.e(LOG_TAG, "Получил сохраненные данные");
                        }
                    }
                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(weatherAtMoment.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo.getState().isFinished()) {
                            //getViewCurrentWeather();
                            Log.e(LOG_TAG, "Погода на текущий момент получена");
                        } else if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                            showErrorMessageInternetMissing(R.string.network_error);
                        }
                    }
                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(weatherFiveDays.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            weatherListView.setAdapter(adapter);
                            Log.e(LOG_TAG, "Погода за пять деней получена");
                        }
                    }
                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(fillDatabase.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            Log.e(LOG_TAG, "Данные в БД внесены");
                        }
                    }
                });
    }

    private void showErrorMessageInternetMissing(int errorText) {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                getApplicationContext().getResources().getString(errorText),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, Gravity.AXIS_X_SHIFT, Gravity.AXIS_Y_SHIFT);
        toast.show();
    }

    private void getViewCurrentWeather(long temp, String description, String icon) {

        ImageView iconImageView = (ImageView) findViewById(R.id.image_view_icon);
        TextView tempTextView = (TextView) findViewById(R.id.text_view_temp);
        TextView descTextView = (TextView) findViewById(R.id.text_view_description);

        try {

            tempTextView.setText(temp + getApplicationContext().getResources().getString(R.string.degree));
            descTextView.setText(String.valueOf(description));

            Glide
                    .with(this)
                    .load(URL_ICON_BEGIN + icon + URL_ICON_END)
                    .into(iconImageView);

        } catch (NullPointerException ex) {
            showErrorMessageInternetMissing(R.string.network_error);
        }
    }


}