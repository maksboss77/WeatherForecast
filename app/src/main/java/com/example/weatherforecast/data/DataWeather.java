package com.example.weatherforecast.data;

import android.util.Log;

import com.example.weatherforecast.DataRequestFromServer;
import com.example.weatherforecast.DateConversion;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.fivedaysjsonschema.Example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Response;

public final class DataWeather {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int WEATHER_OBJECT_INDEX = 0;

    private static final String WEATHER_ARRAY_LIST = "list";

    private static final String WEATHER_OBJECT_DATE = "dt";

    private static final String WEATHER_OBJECT_MAIN = "main";

    private static final String WEATHER_OBJECT_TEMP = "temp";

    private static final String WEATHER_OBJECT_PRESSURE = "pressure";

    private static final String WEATHER_OBJECT_HUMIDITY = "humidity";

    private static final String WEATHER_ARRAY = "weather";

    private static final String WEATHER_OBJECT_DESCRIPTION = "description";

    private static final String WEATHER_OBJECT_ICON = "icon";

    private static final String WEATHER_OBJECT_CLOUDS = "clouds";

    private static final String WEATHER_OBJECT_ALL = "all";

    private static final String WEATHER_OBJECT_WIND = "wind";

    private static final String WEATHER_OBJECT_SPEED = "speed";

    private static final String FIVE_WEATHER_RESPONSE_URL =
            "http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837";
    private static final String CURRENT_WEATHER_RESPONSE_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837";

    private DataWeather() {

    }

    public static ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList, String dateFormat) {

        int averageTemp = 0, countTemp = 0, temp = 0, prevIndex = 0;

        long prevDateMilliseconds = 0;
        String prevDate = "", date = "", icon = "";

        ArrayList<Weather> fiveWeather = new ArrayList<>();
        Weather weather;

        for (int i = 0; i < weatherArrayList.size(); i++) {

            weather = weatherArrayList.get(i);

            date = DateConversion.getDateInMilliseconds(weather.getDate(), dateFormat);
            icon = weather.getIcon();
            temp = weather.getTemp();

            if (prevDate.isEmpty()) {
                prevDate = date;
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            } else if (!date.equals(prevDate)) {

                int t = (int) Math.round(averageTemp/(countTemp*1.0));

                int maxDifferent = 100;

                for (int j = prevIndex; j < i; j++) {
                    Weather searchIcon = weatherArrayList.get(j);

                    if (Math.abs(searchIcon.getTemp() - t) < maxDifferent) {
                        maxDifferent = Math.abs(searchIcon.getTemp() - t);
                        icon = searchIcon.getIcon();
                    }

                }

                Weather averageWeather = new Weather(prevDateMilliseconds, t, icon);

                fiveWeather.add(averageWeather);
                averageTemp = weather.getTemp();
                countTemp = 1;
                prevDate = date;
                prevIndex = i;
            } else {
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            }

        }

        return fiveWeather;

    }

    public static Weather getFiveDaysWeathers(Response<Example> response, int i) {
        long date = getFiveWeatherDate(response, i);
        int temp = getFiveWeatherTemp(response, i);
        int pressure = getFiveWeatherPressure(response, i);
        int clouds = getFiveWeatherClouds(response, i);
        int wind = getFiveWeatherWind(response, i);
        int humidity = getFiveWeatherHumidity(response, i);
        String description = getFiveWeatherDescription(response, i);
        String icon = getFiveWeatherIcon(response, i);

        return new Weather(date, temp, pressure, clouds, wind, humidity, description, icon);
    }

    private static String getFiveWeatherIcon(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getWeather().get(0).getIcon();
    }

    private static String getFiveWeatherDescription(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getWeather().get(0).getDescription();

    }

    private static int getFiveWeatherHumidity(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getMain().getHumidity();
    }

    private static int getFiveWeatherWind(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return (int) Math.round(response.body().getList().get(i).getWind().getSpeed());
    }

    private static int getFiveWeatherClouds(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getClouds().getAll();
    }

    private static int getFiveWeatherPressure(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getMain().getPressure();
    }

    private static int getFiveWeatherTemp(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return (int) Math.round(response.body().getList().get(i).getMain().getTemp());
    }

    private static long getFiveWeatherDate(Response<com.example.weatherforecast.fivedaysjsonschema.Example> response, int i) {
        return response.body().getList().get(i).getDt();
    }
}
