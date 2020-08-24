package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Общая переменная со всей информацией о погоде за 5 дней
    public static ArrayList<Weather> weathers;

    // Общая переменная с краткой информацией о погоде за 5 дней
    public static ArrayList<Weather> weathersFiveDay;

    // Публичная переменная ListView
    public static ListView weatherListView;

    // Adapter для отрисовки 5 дней
    public static WeatherAdapter adapter;

    // Погода сейчас
    public static Weather weather;

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

        weatherListView = (ListView) findViewById(R.id.list);

        // Погода за 5 дней
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class).build();
        // Погода на текущий момент
        OneTimeWorkRequest nowUploadWorkRequest = new OneTimeWorkRequest.Builder(NowUploadWorker.class).build();

        //Запускаем Worker в фоновом потоке, сначала получаем информацию на "Сейчас",
        // потом загружается информация на 5 дней
        WorkManager.getInstance(this)
                .beginWith(nowUploadWorkRequest)
                .then(uploadWorkRequest)
                .enqueue();

        // Когда работа завершена, отрисовать текущую погоду
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(nowUploadWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            getView();
                        }
                    }
                });

        // Отслеживание, когда работа завершена - отрисовать элементы списка
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        // Статус Worker'a
                        System.out.println("Status Worker: " + workInfo.getState().name());
                        if (workInfo.getState().isFinished()) {
                            weatherListView.setAdapter(adapter);
                            System.out.println("Я уже должен был отрисоваться");
                        }
                    }
                });

    }

    private void getView() {

        ImageView iconImageView = (ImageView) findViewById(R.id.image_view_icon);
        TextView tempTextView = (TextView) findViewById(R.id.text_view_temp);
        TextView descTextView = (TextView) findViewById(R.id.text_view_description);

        String urlIconBegin = "http://openweathermap.org/img/wn/";
        String urlIconEnd = "@2x.png";

        Glide
                .with(this)
                .load(urlIconBegin + weather.getIcon() + urlIconEnd)
                .into(iconImageView);

        tempTextView.setText(String.valueOf(weather.getTemp()));
        descTextView.setText(String.valueOf(weather.getDescription()));
    }


}