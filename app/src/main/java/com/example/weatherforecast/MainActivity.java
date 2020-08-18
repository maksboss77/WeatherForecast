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
        ArrayList<Weather> weathers = QueryUtils.extractWeaters();

        ListView weatherListView = (ListView) findViewById(R.id.list);

        WeatherAdapter adapter = new WeatherAdapter(this, 0 ,weathers);

        weatherListView.setAdapter(adapter);

    }
}