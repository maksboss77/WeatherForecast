package com.example.weatherforecast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.ActivityOptions;
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
import com.example.weatherforecast.api.OpenWeatherMapApi;
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

        startWorker();

        testApi();
        // Отслеживание нажатий по элементам
        // При нажатии на элемент списка открываем новый экран (детали) с указанной датой.
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

    private void testApi() {

        //TODO: response.body приходит с нулевыми значениями
        Call<Weather> weatherTest = App.getApi().getCurrentWeathersData(CITY, LANG, UNITS, APP_ID);
        weatherTest.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                System.out.println("response BODY " + response.body());
                System.out.println("response TEST " + response.body().getTemp());
                System.out.println("response2 TEST " + response.body().getTemp());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println("failure " + t);
            }
        });

//        Call<List<Weather>> weatherList = App.getApi().getFiveWeathersData(CITY, LANG, UNITS, APP_ID);
//        weatherList.enqueue(new Callback<List<Weather>>() {
//            @Override
//            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {
//                System.out.println("response " + response.body().size());
//                System.out.println("response2 " + response.body().size());
//            }
//
//            @Override
//            public void onFailure(Call<List<Weather>> call, Throwable t) {
//                System.out.println("failure " + t);
//            }
//        });





        //      Call<Weather> weatherList = App.getApi().getFiveWeathersData(CITY, LANG, UNITS, APP_ID);



//        Call<List<JsonObject>> jsonList = App.getApi().getJsonObject();
//        System.out.println("TEST: " + jsonList);

//        App.getApi().getCurrentWeathersData(CITY, LANG, UNITS, APP_ID).enqueue(new Callback<List<Weather>>() {
//            @Override
//            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {
//                weather.
//            }
//
//            @Override
//            public void onFailure(Call<List<Weather>> call, Throwable t) {
//
//            }
//        });



//        App.getApi().getFiveWeathersData(CITY, LANG, UNITS, APP_ID).enqueue(new Callback<List<Weather>>() {
//            @Override
//            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {
//
//                summaryWeathers.addAll(response.body());
//
//                WeatherJsonAdapter weatherJsonAdapter =
//                        new WeatherJsonAdapter(MainActivity.this, 0, summaryWeathers);
//                weatherListView.setAdapter(weatherJsonAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Weather>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Не удалось установить соединение", Toast.LENGTH_SHORT).show();
//            }
//        });

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
                .then(weatherAtMoment)
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
                        }
                    }
                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(weatherAtMoment.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo.getState().isFinished()) {
                            getViewCurrentWeather();
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

    private void getViewCurrentWeather() {

        ImageView iconImageView = (ImageView) findViewById(R.id.image_view_icon);
        TextView tempTextView = (TextView) findViewById(R.id.text_view_temp);
        TextView descTextView = (TextView) findViewById(R.id.text_view_description);


        try {

            tempTextView.setText(weather.getTemp() + getApplicationContext().getResources().getString(R.string.degree));
            descTextView.setText(String.valueOf(weather.getDescription()));

            Glide
                    .with(this)
                    .load(URL_ICON_BEGIN + weather.getIcon() + URL_ICON_END)
                    .into(iconImageView);

        } catch (NullPointerException ex) {
            showErrorMessageInternetMissing(R.string.network_error);
        }
    }


}