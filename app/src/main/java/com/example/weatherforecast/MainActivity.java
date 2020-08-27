package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.content.Intent;
import android.os.Bundle;
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

        weatherListView = (ListView) findViewById(R.id.list);

        // Критерий: подключен Wi-Fi или мобильная передача данных
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        // Погода за 5 дней
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest
                .Builder(UploadWorker.class)
                .setConstraints(constraints)
                .build();
        // Погода на текущий момент
        OneTimeWorkRequest nowUploadWorkRequest = new OneTimeWorkRequest
                .Builder(NowUploadWorker.class)
                .setConstraints(constraints)
                .build();
        // Заполнение БД данными о погоде
        OneTimeWorkRequest addDatabaseWork = new OneTimeWorkRequest
                .Builder(AddDataWorker.class)
                .setConstraints(constraints)
                .build();

        // Берем и заполняем данные из кэша
        OneTimeWorkRequest cashDatabaseWork = new OneTimeWorkRequest
                .Builder(CashDatabaseWorker.class)
                .build();

        //Запускаем Worker в фоновом потоке, сначала получаем информацию на "Сейчас",
        // потом загружается информация на 5 дней

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.INVISIBLE);


        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

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
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });

        // Отрисовка элементов списка после выполнения запроса (uploadWorkRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        // Статус Worker'a
                        System.out.println("Status Worker: " + workInfo.getState().name());
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

                        }
                    }
                });



        // Отслеживание нажатий по элементам
        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });


    }

    private void showToast(ArrayList<Weather> all) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < all.size(); i++) {
            builder.append(all.get(i).toString()).append("\n");
        }

        Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
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
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


}