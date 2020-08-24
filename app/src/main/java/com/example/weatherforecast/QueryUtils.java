package com.example.weatherforecast;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class QueryUtils {


    private static final String WEATHER_RESPONSE_URL =
            "http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837";

    // Частный конструктор - то есть никто не должен создавать экземпляр этого класса.
    private QueryUtils() {

    }

    public static ArrayList<Weather> extractWeathers() {

        ArrayList<Weather> weathers = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(getStringJSON(WEATHER_RESPONSE_URL));
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
                    JSONObject currentWeatherListObject = weatherList.getJSONObject(j);
                    description = currentWeatherListObject.getString("description");

                    icon = currentWeatherListObject.getString("icon");

                }

                JSONObject cloudsObject = currentWeatherObject.getJSONObject("clouds");
                int clouds = cloudsObject.getInt("all");

                JSONObject windObject = currentWeatherObject.getJSONObject("wind");
                int wind = windObject.getInt("speed");

                Weather weather = new Weather(date, temp, pressure, clouds, wind, humidity, description, icon);
                weathers.add(weather);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the weather JSON results", e);
        } catch (Exception e) {
            Log.e("QueryUtils", "Problem URL connect", e);
        }


        return weathers;
    }

    public static String getStringJSON(String urlString) throws IOException {

        String jsonResponse = "";

        if (urlString == null) {
            return jsonResponse;
        }

        URL url = null;

        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = null;
        InputStream inputStream = null;

        try {
            url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect(); //тут вылетает

            stringBuilder = new StringBuilder();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e("QueryUtils", "Problem URLConnection or inputStream connection", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }

        return stringBuilder.toString();
    }


}
