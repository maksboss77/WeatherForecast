package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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


        WeatherAsyncTask task = new WeatherAsyncTask();
        task.execute();

    }

    private class WeatherAsyncTask extends AsyncTask<ListView, Void, WeatherAdapter> {

        ListView weatherListView;

        @Override
        protected WeatherAdapter doInBackground(ListView... listViews) {

            ArrayList<Weather> weathers = QueryUtils.extractWeathers();

            ArrayList<Weather> weathersFiveDay = getFiveDays(weathers);

            weatherListView = (ListView) findViewById(R.id.list);

            WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, 0, weathersFiveDay);

            return adapter;
        }

        @Override
        protected void onPostExecute(WeatherAdapter adapter) {

            weatherListView.setAdapter(adapter);
        }

        // Average temperature for the day
        private int averageTemp;

        // Count temperature for the day
        private int countTemp;

        // Previous day
        private String prevDate = "";
        private long prevDateMilliseconds = 0;

        protected ArrayList<Weather> getFiveDays( ArrayList<Weather> weatherArrayList ) {

            ArrayList<Weather> fiveWeather = new ArrayList<>();

            Weather weather;

            String date = "";
            String icon;
            int temp;

            for (int i = 0; i < weatherArrayList.size(); i++) {

                weather = weatherArrayList.get(i);

                date = getDateString(weather.getDate());
                icon = weather.getIcon();
                temp = weather.getTemp();

                if (prevDate.isEmpty()) {
                    prevDate = date;
                    prevDateMilliseconds = weather.getDate();
                    averageTemp += temp;
                    countTemp++;
                    prevDateMilliseconds = weather.getDate();
                } else if (!date.equals(prevDate)) {
                    //TODO: Return average icon
                    Weather averageWeather = new Weather(prevDateMilliseconds, averageTemp/countTemp,
                            0, 0, 0, 0, "", icon);
                    fiveWeather.add(averageWeather);
                    averageTemp = 0;
                    countTemp = 0;
                    prevDate = date;
                } else {
                    averageTemp += temp;
                    countTemp++;
                    prevDateMilliseconds = weather.getDate();
                }



            }

            return fiveWeather;

        }

        private String getDateString(long timeInMilliseconds) {

            Calendar today = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMilliseconds * 1000L);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
                return "Сегодня";
            else
                today.add(Calendar.DATE, +1);

            if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
                return "Завтра";

            return simpleDateFormat.format(calendar.getTime());
        }

    }

}