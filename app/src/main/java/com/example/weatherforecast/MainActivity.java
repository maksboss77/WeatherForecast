package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * Сегодня и на 4 последующих дня
     * http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837
     *
     *
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создаем список фейковых дней
        ArrayList<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather(12491212, 28, 34, 34, 2, 45, "description1", "icon12"));
        weathers.add(new Weather(12491213, 21, 2, 65, 3, 32, "description2", "icon13"));
        weathers.add(new Weather(12491214, 25, 12, 4, 4, 12, "description3", "icon14"));
        weathers.add(new Weather(12491215, 31, 4, 34, 9, 2, "description4", "icon15"));


        ListView weatherListView = (ListView) findViewById(R.id.list);

        WeatherAdapter adapter = new WeatherAdapter(this, 0 ,weathers);

        weatherListView.setAdapter(adapter);

    }
}