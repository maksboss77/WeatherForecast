package com.example.weatherforecast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.Pair;
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
import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.data.WeatherDao;
import com.example.weatherforecast.worker.AddDataWorker;
import com.example.weatherforecast.worker.NowUploadWorker;
import com.example.weatherforecast.worker.UploadWorker;
import com.example.weatherforecast.worker.CashDatabaseWorker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Имя класса
    private static final String LOG_TAG = MainActivity.class.getName();

    // Имя нажатой позиции
    private static final String KEY = "index";

    // Общая переменная со всей информацией о погоде за 5 дней
    public static ArrayList<Weather> weathers;

    // Общая переменная с краткой информацией о погоде за 5 дней
    public static ArrayList<Weather> weathersFiveDay;

    // Публичная переменная ListView
    public ListView weatherListView;

    // Adapter для отрисовки 5 дней
    public static WeatherAdapter adapter;

    // Погода сейчас
    public static Weather weather;

    //
    public static WeatherDao weatherDao;


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

        // Критерий: подключен Wi-Fi или мобильная передача данных
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        // Берем и заполняем данные из кэша
        OneTimeWorkRequest cashDatabaseWork = new OneTimeWorkRequest
                .Builder(CashDatabaseWorker.class)
                .build();

        // Погода на текущий момент
        OneTimeWorkRequest nowUploadWorkRequest = new OneTimeWorkRequest
                .Builder(NowUploadWorker.class)
                .setConstraints(constraints)
                .build();

        // Погода за 5 дней
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest
                .Builder(UploadWorker.class)
                .setConstraints(constraints)
                .build();

        // Заполнение БД данными о погоде
        OneTimeWorkRequest addDatabaseWork = new OneTimeWorkRequest
                .Builder(AddDataWorker.class)
                .setConstraints(constraints)
                .build();

        // Запускаем Worker в фоновом потоке:
        // 1. Читаем данные из Бд и отображаем ее
        // 2. Делаем запрос на получение текущей погоды
        // 3. Делаем запрос на получение погоды на 5 дней
        // 4. Обновляем данные в БД.
        // Примечание: каждый пункт выполяется, если выполнены прерыдущие действия цепочки.

        // Скрываем макет, отображаем Progress Bar
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.INVISIBLE);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Запускаем работу (в фоновом потоке) на выполнение
        WorkManager.getInstance(this)
                .beginWith(cashDatabaseWork)
                .then(nowUploadWorkRequest)
                .then(uploadWorkRequest)
                .then(addDatabaseWork)
                .enqueue();


        // Отрисовка списка из кэша (cashDatabaseWork)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(cashDatabaseWork.getId())
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

        // Отрисовка текущей погоды после выполнения (nowUploadWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(nowUploadWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo.getState().isFinished()) {
                            getView();
                        } else if (workInfo.getState() == WorkInfo.State.ENQUEUED){
                            // если задача не выполнена и стоит в очереди, значит соединение
                            // с сетью отсуствует и нужно вывести соотвествующее сообщение
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    getApplicationContext().getResources().getString(R.string.network_error),
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, Gravity.AXIS_X_SHIFT, Gravity.AXIS_Y_SHIFT);
                            toast.show();
                        }
                    }
                });

        // Отрисовка элементов списка после выполнения запроса (uploadWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            weatherListView.setAdapter(adapter);
                        }
                    }
                });

        // Действие после вставки строк (addDatabaseWork)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(addDatabaseWork.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            Log.e(LOG_TAG, "Данные в БД внесены");
                        }
                    }
                });



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


    String urlIconBegin = "http://openweathermap.org/img/wn/";
    String urlIconEnd = "@2x.png";

    private void getView() {

        ImageView iconImageView = (ImageView) findViewById(R.id.image_view_icon);
        TextView tempTextView = (TextView) findViewById(R.id.text_view_temp);
        TextView descTextView = (TextView) findViewById(R.id.text_view_description);


        try {
            Glide
                    .with(this)
                    .load(urlIconBegin + weather.getIcon() + urlIconEnd)
                    .into(iconImageView);

            tempTextView.setText(weather.getTemp() + getApplicationContext().getResources().getString(R.string.degree));
            descTextView.setText(String.valueOf(weather.getDescription()));
        } catch (NullPointerException ex) {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.network_error),
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, Gravity.AXIS_X_SHIFT, Gravity.AXIS_Y_SHIFT);
            toast.show();
        }
    }


}