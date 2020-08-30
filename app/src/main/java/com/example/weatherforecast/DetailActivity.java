package com.example.weatherforecast;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.data.WeatherDao;
import com.example.weatherforecast.worker.ChartWorker;
import com.example.weatherforecast.worker.ReadDetailsWorker;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


public class DetailActivity extends AppCompatActivity {

    public static ListView detailListView;

    public static DetailsAdapter adapter;

    public static ArrayList<Weather> detailsWeathers;

    public static WeatherDao detailsDao;

    public static LineChart chart;

    public static int index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Получить индекс нажатого элемента (если нажатие произошло после 22,
        // то нажатие на 1 элемент - это нажатие на "завтра"
        index = getIntent().getExtras().getInt("index") + getIndexTime();


        setContentView(R.layout.activity_detail);

        /** Установка кнопки "Назад", если делать через манифест, то страница перезагружается*/
        ImageButton button = (ImageButton) findViewById(R.id.button_prev);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                onBackPressed();
//                finishAfterTransition();
            }
        });

        // Установить заголовок, как дату
        TextView title = (TextView) findViewById(R.id.text_title);
        title.setText(getDate());

        // График
        chart = (LineChart) findViewById(R.id.chart);

        detailListView = (ListView) findViewById(R.id.list);

        // Отправить запрос в фоновом потоке на прочтение данных с базы, а после отрисовать график
        OneTimeWorkRequest readDatabase = new OneTimeWorkRequest.Builder(ReadDetailsWorker.class).build();
        OneTimeWorkRequest viewChart = new OneTimeWorkRequest.Builder(ChartWorker.class).build();
        WorkManager.getInstance(this)
                .beginWith(readDatabase)
                .then(viewChart)
                .enqueue();

        // Отрисовать список, если прочитали данные с бд
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(readDatabase.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            detailListView.setAdapter(adapter);
                        }
                    }
                });

        // Перезагрузить график
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(viewChart.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            chart.invalidate();
                        }
                    }
                });

    }

    // Получить время (для отображения информации и заголовка)
    // Если время больше 22 чаов, то отображать список на сегодняшний день не нужно, так как данных нет
    private int getIndexTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String t = simpleDateFormat.format(calendar.getTime());
        int result = Integer.parseInt(t);

        if (result >= 22)
            return  1;
        else
            return 0;

    }

    private String getDate() {

        if (index == 0)
            return "Сегодня";
        if (index == 1)
            return "Завтра";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, index);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");

        return simpleDateFormat.format(calendar.getTime());
    }

}
