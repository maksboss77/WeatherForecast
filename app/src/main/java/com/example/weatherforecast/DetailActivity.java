package com.example.weatherforecast;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.data.WeatherDao;
import com.example.weatherforecast.worker.ReadDetailsWorker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


public class DetailActivity extends AppCompatActivity {

    public ListView detailListView;

    public static ArrayList<Weather> detailsWeathers;

    public static WeatherDao detailsDao;

    public static int index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Получить индекс нажатого элемента
        index = getIntent().getExtras().getInt("index");

        setContentView(R.layout.activity_detail);

        detailListView = (ListView) findViewById(R.id.list_details);

        // Отправить запрос в фоновом потоке на прочтение данных с базы
        OneTimeWorkRequest readDatabase = new OneTimeWorkRequest.Builder(ReadDetailsWorker.class).build();
        WorkManager.getInstance(this)
                .enqueue(readDatabase);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(readDatabase.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            //set Adapter
                            for (int i = 0; i < detailsWeathers.size(); i++) {
                                System.out.println("INDEX[" + i +"]: " + detailsWeathers.get(i).toString() + "\n\n");
                            }
                        }
                    }
                });

    }

    String urlIconBegin = "http://openweathermap.org/img/wn/";
    String urlIconEnd = "@2x.png";

//    private void getView() {
//
//        TextView dateTimeTextView = (TextView) findViewById(R.id.details_date_time);
//        TextView tempTextView = (TextView) findViewById(R.id.details_temp);
//        TextView pressureTextView = (TextView) findViewById(R.id.details_pressure);
//        TextView cloudsTextView = (TextView) findViewById(R.id.details_clouds);
//        TextView windTextView = (TextView) findViewById(R.id.details_wind);
//        TextView humidityTextView = (TextView) findViewById(R.id.details_humidity);
//        TextView descriptionTextView = (TextView) findViewById(R.id.details_description);
//
//
//
//        ImageView iconImageView = (ImageView) findViewById(R.id.details_icon);
//        Glide
//                .with(this)
//                .load(urlIconBegin + detailsWeathers. + urlIconEnd)
//                .into(iconImageView);
//
//     }
}
