package com.example.weatherforecast;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class QueryUtils {

    private static final String SAMPLE_JSON_RESPONSE = "";

    // Частный конструктор - то есть никто не должен создавать экземпляр этого класса.
    private QueryUtils() {

    }

    public static ArrayList<Weather> extractWeaters() {

        ArrayList<Weather> weathers = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray weatherArrayList = baseJsonResponse.getJSONArray("list");

            for (int i = 0; i < weatherArrayList.length(); i++) {
                JSONObject currentWeatherObject = weatherArrayList.getJSONObject(i);
                int date = currentWeatherObject.getInt("dt");

                JSONObject mainObject = currentWeatherObject.getJSONObject("main");
                int temp = mainObject.getInt("temp");
                int pressure = mainObject.getInt("pressure");
                int humidity = mainObject.getInt("humidity");

                JSONArray weatherList = currentWeatherObject.getJSONArray("weather");
                String description = "";
                String icon = "";
                for (int j = 0; j < weatherList.length(); j++) {
                    JSONObject currentWeatherListObject = weatherList.getJSONObject(i);
                    description = currentWeatherListObject.getString("description");
                    icon = currentWeatherListObject.getString("icon");

                }

                JSONObject cloudsObject = currentWeatherObject.getJSONObject("clouds");
                int clouds = cloudsObject.getInt("all");

                JSONObject windObject = currentWeatherObject.getJSONObject("wind");
                int wind = cloudsObject.getInt("speed");

                Weather weather = new Weather(date, temp, pressure, clouds, wind, humidity, description, icon);
                weathers.add(weather);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the weather JSON results", e);
        }


        return weathers;
    }


}
