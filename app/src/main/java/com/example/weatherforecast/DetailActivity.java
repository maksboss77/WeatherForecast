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
import com.example.weatherforecast.worker.ReadDetailsWorker;

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

    public static int index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Получить индекс нажатого элемента
        index = getIntent().getExtras().getInt("index");
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

        detailListView = (ListView) findViewById(R.id.list);

        // Отправить запрос в фоновом потоке на прочтение данных с базы
        OneTimeWorkRequest readDatabase = new OneTimeWorkRequest.Builder(ReadDetailsWorker.class).build();
        WorkManager.getInstance(this)
                .enqueue(readDatabase);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(readDatabase.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {

                            detailListView.setAdapter(adapter);

                            for (int i = 0; i < detailsWeathers.size(); i++) {
                                System.out.println("INDEX[" + i + "]: " + detailsWeathers.get(i).toString() + "\n\n");
                            }
                        }
                    }
                });

    }

    private String getDate() {

        if (index == 0)
            return "Сегодня";
        if (index == 1)
            return "Завтра";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, index);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return simpleDateFormat.format(calendar.getTime());
    }

}
